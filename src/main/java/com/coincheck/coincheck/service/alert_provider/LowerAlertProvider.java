package com.coincheck.coincheck.service.alert_provider;

import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.model.AlertType;
import com.coincheck.coincheck.model.MarketCap;
import com.coincheck.coincheck.repository.SentAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LowerAlertProvider implements AlertProvider {

    private final SentAlertRepository sentAlertRepository;

    @Override
    public Optional<Alert> getAlert(MarketCap marketCap, LocalDateTime cooldownStart) {
        int alertsInCooldown = sentAlertRepository.countByCoinAndAlertTypesContainsAndSentDateAfter(marketCap.getCoin(), AlertType.LOWER, cooldownStart);

        Float lowerMillion = marketCap.getCoin().getAlertConfiguration().getLowerMillion();

        if (lowerMillion != null && alertsInCooldown == 0) {
            BigDecimal lowerLimitDollars = BigDecimal.valueOf(lowerMillion * 1e6);
            if (marketCap.getMarketCapUsd().compareTo(lowerLimitDollars) <= 0) {
                return Optional.of(new Alert(marketCap, AlertType.LOWER, null));
            }
        }
        return Optional.empty();
    }
}