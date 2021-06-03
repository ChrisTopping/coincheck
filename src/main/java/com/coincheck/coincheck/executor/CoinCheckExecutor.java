package com.coincheck.coincheck.executor;

import com.coincheck.coincheck.configuration.CoinProperties;
import com.coincheck.coincheck.model.MarketCap;
import com.coincheck.coincheck.repository.MarketCapRepository;
import com.coincheck.coincheck.service.CoinCheckService;
import com.coincheck.coincheck.service.MarketCapAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoinCheckExecutor {

    private final CoinCheckService coinCheckService;
    private final CoinProperties coinProperties;
    private final MarketCapRepository marketCapRepository;
    private final MarketCapAlertService marketCapAlertService;


    @Scheduled(fixedRateString = "${call-rate-milliseconds}")
    public void checkCoins() {
        log.info("Checking");

        coinProperties.getCoins()
                .forEach(coin -> {
                    MarketCap marketCap = coinCheckService.getMarketCap(coin);
                    marketCapRepository.save(marketCap);
                    marketCapAlertService.alert(marketCap);
                    BigDecimal result = marketCap.getMarketCapUsd();
                    log.info("Market cap of " + coin + " is " + result);
                });
    }

}
