package com.coincheck.coincheck.service;

import com.coincheck.coincheck.model.AlertConfiguration;
import com.coincheck.coincheck.model.Coin;
import com.coincheck.coincheck.repository.MarketCapRepository;
import com.coincheck.coincheck.repository.SentAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TtlService {

    private final MarketCapRepository marketCapRepository;
    private final SentAlertRepository sentAlertRepository;

    public void deleteMarketCapsBeforeChangeWindow(List<Coin> coins) {
        coins.stream()
                .map(Coin::getAlertConfiguration)
                .map(config -> {
                    return Stream.of(config.getIncreaseMinutes(), config.getDecreaseMinutes())
                            .filter(Objects::nonNull)
                            .max(Integer::compareTo);
                }).filter(Optional::isPresent)
                .map(Optional::get)
                .max(Integer::compareTo)
                .map(changeWindowMinutes -> LocalDateTime.now().minusMinutes(changeWindowMinutes))
                .ifPresent(marketCapRepository::deleteByCreatedDateBefore);
    }

    public void deleteAlertsBeforeCooldown(List<Coin> coins) {
        coins.stream()
                .map(Coin::getAlertConfiguration)
                .map(AlertConfiguration::getCooldownMinutes)
                .max(Integer::compareTo)
                .map(cooldownMinutes -> LocalDateTime.now().minusMinutes(cooldownMinutes))
                .ifPresent(sentAlertRepository::deleteBySentDateBefore);
    }

}
