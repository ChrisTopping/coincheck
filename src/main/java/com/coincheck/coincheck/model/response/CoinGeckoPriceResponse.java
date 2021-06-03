package com.coincheck.coincheck.model.response;

import lombok.Data;

@Data
public class CoinGeckoPriceResponse {
    private double usd;
    private double bnb;
    private long last_updated_at;
}
