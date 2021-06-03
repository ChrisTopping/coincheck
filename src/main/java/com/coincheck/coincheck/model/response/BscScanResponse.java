package com.coincheck.coincheck.model.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BscScanResponse {
    private int status;
    private String message;
    private BigDecimal result;
}
