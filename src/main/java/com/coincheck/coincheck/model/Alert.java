package com.coincheck.coincheck.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    private MarketCap marketCap;
    private AlertType alertType;
    private MarketCap referenceMarketCap;
}
