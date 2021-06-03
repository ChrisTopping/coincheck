package com.coincheck.coincheck.service.email_contributor;

import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.model.AlertType;
import org.springframework.stereotype.Service;

@Service
public class UpperEmailContributor implements EmailContributor {

    @Override
    public boolean canContribute(AlertType alertType) {
        return AlertType.UPPER.equals(alertType);
    }

    @Override
    public String contribute(Alert alert) {
        float thresholdMillions = alert.getMarketCap().getCoin().getAlertConfiguration().getUpperMillion();
        return String.format("Market cap is above the upper threshold ($%,.2fm)", thresholdMillions);
    }
}
