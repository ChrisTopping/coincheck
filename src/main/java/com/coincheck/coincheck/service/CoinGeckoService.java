package com.coincheck.coincheck.service;

import com.coincheck.coincheck.model.response.CoinGeckoPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoinGeckoService {

    private final RestTemplate template;

    public CoinGeckoPriceResponse getCoinPrice(String platformId, String contractAddress) {
        UriComponents uriComponent = UriComponentsBuilder.fromHttpUrl("https://api.coingecko.com/api/v3/simple/token_price/" + platformId)
                .queryParam("contract_addresses", contractAddress)
                .queryParam("vs_currencies", "usd,bnb")
                .queryParam("include_last_updated_at", true)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, CoinGeckoPriceResponse>> response = template.exchange(
                uriComponent.toString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, CoinGeckoPriceResponse>>() {
                }
        );

        return response.getBody().get(contractAddress);
    }

}
