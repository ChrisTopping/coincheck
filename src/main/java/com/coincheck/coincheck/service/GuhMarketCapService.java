package com.coincheck.coincheck.service;

import com.coincheck.coincheck.model.Coin;
import com.coincheck.coincheck.model.MarketCap;
import com.coincheck.coincheck.model.request.BscDataseedRequest;
import com.coincheck.coincheck.model.response.BscDataseedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class GuhMarketCapService {

    private static final String GUH_CONTRACT_ADDRESS = "0x42069c0cf4da25420fc4c9d9001ba5af7846ccfd";

    private final RestTemplate template;

    @Value("${calculate-guh-using-bsc-dataseed:true}")
    private boolean calculateGuhUsingBscDataseed;

    public boolean isGuh(Coin coin) {
        return calculateGuhUsingBscDataseed && GUH_CONTRACT_ADDRESS.equalsIgnoreCase(coin.getContractAddress());
    }

    public MarketCap getGuhMarketCap(Coin coin) {
        double coinPrice = getGuhCoinPrice();
        double totalSupply = getGuhTotalSupply();

        return MarketCap
                .builder()
                .marketCapUsd(new BigDecimal(totalSupply * coinPrice))
                .coin(coin)
                .fromDateTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }

    private double getGuhCoinPrice() {
        return callBscDataseedForTokenAndId("0x3bc5de30", 5, "0x456b450f7d9e033418ae26c357f8b83ad3d1f172");
    }

    private double getGuhTotalSupply() {
        return callBscDataseedForTokenAndId("0x18160ddd", 6, "0x42069c0cf4da25420fc4c9d9001ba5af7846ccfd");
    }

    private double callBscDataseedForTokenAndId(String hash, int id, String to) {
        BscDataseedRequest request = BscDataseedRequest
                .builder()
                .jsonrpc("2.0")
                .id(id)
                .method("eth_call")
                .params(asList(
                        BscDataseedRequest.Params
                                .builder()
                                .data(hash)
                                .to(to)
                                .build(),
                        "latest"
                        )
                ).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<BscDataseedRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<BscDataseedResponse> response = template.exchange(
                "https://bsc-dataseed1.binance.org/",
                HttpMethod.POST,
                entity,
                BscDataseedResponse.class
        );

        return new BigInteger(response.getBody().getResult().substring(2), 16).longValue() / 1000000000.0;
    }

}
