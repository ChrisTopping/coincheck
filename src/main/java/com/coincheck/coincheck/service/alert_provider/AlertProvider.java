package com.coincheck.coincheck.service.alert_provider;

import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.model.MarketCap;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AlertProvider {
    Optional<Alert> getAlert(MarketCap marketCap, LocalDateTime cooldownStart);
}
