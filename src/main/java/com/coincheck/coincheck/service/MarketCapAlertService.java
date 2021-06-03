package com.coincheck.coincheck.service;

import com.coincheck.coincheck.model.*;
import com.coincheck.coincheck.repository.MarketCapRepository;
import com.coincheck.coincheck.repository.SentAlertRepository;
import com.coincheck.coincheck.service.alert_provider.AlertProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketCapAlertService {

    private final SentAlertRepository sentAlertRepository;
    private final EmailService emailService;
    private final List<AlertProvider> alertProviders;

    public void alert(MarketCap marketCap) {
        Coin coin = marketCap.getCoin();
        int cooldownMinutes = coin.getAlertConfiguration().getCooldownMinutes();
        LocalDateTime cooldownStart = LocalDateTime.now().minusMinutes(cooldownMinutes);

        List<Alert> alerts = alertProviders.stream()
                .map(alertProvider -> alertProvider.getAlert(marketCap, cooldownStart))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (!alerts.isEmpty()) {
            emailService.emailAlerts(alerts);
            sentAlertRepository.save(mapToSentAlert(marketCap, alerts));
        }
    }

    private SentAlert mapToSentAlert(MarketCap marketCap, List<Alert> alerts) {
        List<AlertType> alertTypes = alerts
                .stream()
                .map(Alert::getAlertType)
                .collect(Collectors.toList());

        return SentAlert.builder()
                .marketCap(marketCap)
                .coin(marketCap.getCoin())
                .alertTypes(alertTypes)
                .build();
    }

}
