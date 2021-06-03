package com.coincheck.coincheck.service;

import com.coincheck.coincheck.model.response.BscScanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BscScanService {

    private final RestTemplate template;

    @Value("${bsc-scan-api-key}")
    private String apiKey;

    public BscScanResponse getMarketSupply(String contractAddress) {
        UriComponents uriComponent = UriComponentsBuilder.fromHttpUrl("https://api.bscscan.com/api")
                .queryParam("module", "stats")
                .queryParam("action", "tokenCsupply")
                .queryParam("apikey", apiKey)
                .queryParam("contractaddress", contractAddress)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<BscScanResponse> response = template.exchange(
                uriComponent.toString(),
                HttpMethod.GET,
                entity,
                BscScanResponse.class
        );

        return response.getBody();
    }

}
