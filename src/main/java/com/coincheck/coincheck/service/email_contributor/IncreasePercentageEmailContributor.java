package com.coincheck.coincheck.service.email_contributor;

import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.model.AlertType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class IncreasePercentageEmailContributor implements EmailContributor {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    @Override
    public boolean canContribute(AlertType alertType) {
        return AlertType.INCREASE_PERCENTAGE.equals(alertType);
    }

    @Override
    public String contribute(Alert alert) {
        BigDecimal old = alert.getReferenceMarketCap().getMarketCapUsd();
        BigDecimal current = alert.getMarketCap().getMarketCapUsd();

        double increasePercentage = current.subtract(old).divide(old, 5, RoundingMode.DOWN).doubleValue() * 100;
        LocalDateTime oldDate = alert.getReferenceMarketCap().getCreatedDate();

        return String.format("Market cap has increased by %,.0f%% since %s", increasePercentage, oldDate.format(formatter));
    }
}
