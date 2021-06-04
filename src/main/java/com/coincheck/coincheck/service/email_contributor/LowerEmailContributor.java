package com.coincheck.coincheck.service.email_contributor;

import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.model.AlertType;
import org.springframework.stereotype.Service;

@Service
public class LowerEmailContributor implements EmailContributor {

    @Override
    public boolean canContribute(AlertType alertType) {
        return AlertType.LOWER.equals(alertType);
    }

    @Override
    public String contribute(Alert alert) {
        float thresholdMillions = alert.getMarketCap().getCoin().getAlertConfiguration().getLowerMillion();
        return String.format("Market cap is below the lower threshold ($%,.2fm)", thresholdMillions);
    }
}
