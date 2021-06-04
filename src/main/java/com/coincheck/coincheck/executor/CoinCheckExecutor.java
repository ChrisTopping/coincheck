package com.coincheck.coincheck.executor;

import com.coincheck.coincheck.configuration.CoinProperties;
import com.coincheck.coincheck.model.Coin;
import com.coincheck.coincheck.model.MarketCap;
import com.coincheck.coincheck.repository.MarketCapRepository;
import com.coincheck.coincheck.service.CoinCheckService;
import com.coincheck.coincheck.service.MarketCapAlertService;
import com.coincheck.coincheck.service.TtlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CoinCheckExecutor {

    private final CoinProperties coinProperties;
    private final TtlService ttlService;
    private final CoinCheckService coinCheckService;
    private final MarketCapRepository marketCapRepository;
    private final MarketCapAlertService marketCapAlertService;

    @Transactional
    @Scheduled(fixedRateString = "${call-rate-milliseconds}")
    public void checkCoins() {
        List<Coin> coins = coinProperties.getCoins();

        ttlService.deleteAlertsBeforeCooldown(coins);
        ttlService.deleteMarketCapsBeforeChangeWindow(coins);

        coins.forEach(coin -> {
            log.info("Checking market cap of " + coin.getName());
            MarketCap marketCap = coinCheckService.getMarketCap(coin);
            marketCapRepository.save(marketCap);
            marketCapAlertService.alert(marketCap);
            BigDecimal result = marketCap.getMarketCapUsd();
            log.info("Market cap of " + coin.getName() + " is " + result);
        });
    }

}
