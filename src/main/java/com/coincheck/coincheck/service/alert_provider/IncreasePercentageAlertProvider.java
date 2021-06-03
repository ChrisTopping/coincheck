package com.coincheck.coincheck.service.alert_provider;

import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.model.AlertType;
import com.coincheck.coincheck.model.MarketCap;
import com.coincheck.coincheck.repository.MarketCapRepository;
import com.coincheck.coincheck.repository.SentAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IncreasePercentageAlertProvider implements AlertProvider {

    private final SentAlertRepository sentAlertRepository;
    private final MarketCapRepository marketCapRepository;

    @Override
    public Optional<Alert> getAlert(MarketCap marketCap, LocalDateTime cooldownStart) {
        int alertsInCooldown = sentAlertRepository.countByCoinAndAlertTypesContainsAndSentDateAfter(marketCap.getCoin(), AlertType.INCREASE_PERCENTAGE, cooldownStart);

        Integer increasePercentage = marketCap.getCoin().getAlertConfiguration().getIncreasePercentage();
        Integer increaseMinutes = marketCap.getCoin().getAlertConfiguration().getIncreaseMinutes();

        if (increasePercentage != null && increaseMinutes != null && alertsInCooldown == 0) {
            double thresholdIncreaseFactor = increasePercentage / 100d;

            LocalDateTime createdDateStart = LocalDateTime.now().minusMinutes(increaseMinutes);

            Optional<MarketCap> optionalMinMarketCap = marketCapRepository
                    .findAllByCoinAndCreatedDateAfter(marketCap.getCoin(), createdDateStart)
                    .stream()
                    .min(Comparator.comparing(MarketCap::getMarketCapUsd));

            if (optionalMinMarketCap.isPresent()) {
                MarketCap minMarketCap = optionalMinMarketCap.get();
                BigDecimal min = minMarketCap.getMarketCapUsd();
                BigDecimal current = marketCap.getMarketCapUsd();

                double increaseFactor = current.subtract(min).divide(min, 2, RoundingMode.DOWN).doubleValue();
                if (increaseFactor >= thresholdIncreaseFactor) {
                    return Optional.of(new Alert(marketCap, AlertType.INCREASE_PERCENTAGE, minMarketCap));
                }
            }
        }
        return Optional.empty();
    }
}
