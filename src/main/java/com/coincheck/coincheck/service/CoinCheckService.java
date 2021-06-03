package com.coincheck.coincheck.service;

import com.coincheck.coincheck.model.MarketCap;
import com.coincheck.coincheck.model.response.BscScanResponse;
import com.coincheck.coincheck.model.response.CoinGeckoPriceResponse;
import com.coincheck.coincheck.model.Coin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CoinCheckService {

    private final BscScanService bscScanService;
    private final CoinGeckoService coinGeckoService;

    public MarketCap getMarketCap(Coin coin) {
        BscScanResponse marketSupply = bscScanService.getMarketSupply(coin.getContractAddress());
        CoinGeckoPriceResponse coinPrice = coinGeckoService.getCoinPrice(coin.getPlatformId(), coin.getContractAddress());
        return MarketCap
                .builder()
                .coin(coin)
                .marketCapUsd(BigDecimal.valueOf(marketSupply.getResult().longValue() * 1e-9 * coinPrice.getUsd()))
                .build();
    }

}
