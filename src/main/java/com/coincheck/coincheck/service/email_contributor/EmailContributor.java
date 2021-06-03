package com.coincheck.coincheck.service.email_contributor;

import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.model.AlertType;

public interface EmailContributor {
    boolean canContribute(AlertType alertType);
    String contribute(Alert alert);
}
