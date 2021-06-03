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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DecreasePercentageAlertProvider implements AlertProvider {

    private final SentAlertRepository sentAlertRepository;
    private final MarketCapRepository marketCapRepository;

    @Override
    public Optional<Alert> getAlert(MarketCap marketCap, LocalDateTime cooldownStart) {
        int alertsInCooldown = sentAlertRepository.countByCoinAndAlertTypesContainsAndSentDateAfter(marketCap.getCoin(), AlertType.DECREASE_PERCENTAGE, cooldownStart);

        Integer decreasePercentage = marketCap.getCoin().getAlertConfiguration().getDecreasePercentage();
        Integer decreaseMinutes = marketCap.getCoin().getAlertConfiguration().getDecreaseMinutes();

        if (decreasePercentage != null && decreaseMinutes != null && alertsInCooldown == 0) {
            double thresholdDecreaseFactor = -decreasePercentage / 100d;
            LocalDateTime createdDateStart = LocalDateTime.now().minusMinutes(decreaseMinutes);

            Optional<MarketCap> optionalMaxMarketCap = marketCapRepository
                    .findAllByCoinAndCreatedDateAfter(marketCap.getCoin(), createdDateStart)
                    .stream()
                    .max(Comparator.comparing(MarketCap::getMarketCapUsd));

            if (optionalMaxMarketCap.isPresent()) {
                MarketCap maxMarketCap = optionalMaxMarketCap.get();
                BigDecimal max = maxMarketCap.getMarketCapUsd();
                BigDecimal current = marketCap.getMarketCapUsd();

                double decreaseFactor = current.subtract(max).divide(max, 2, RoundingMode.DOWN).doubleValue();
                if (decreaseFactor <= thresholdDecreaseFactor) {
                    return Optional.of(new Alert(marketCap, AlertType.DECREASE_PERCENTAGE, maxMarketCap));
                }
            }
        }
        return Optional.empty();
    }
}
