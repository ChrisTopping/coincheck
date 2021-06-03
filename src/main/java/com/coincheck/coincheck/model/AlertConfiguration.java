package com.coincheck.coincheck.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AlertConfiguration {
    private Float upperMillion;
    private Float lowerMillion;
    private Integer increasePercentage;
    private Integer increaseMinutes;
    private Integer decreasePercentage;
    private Integer decreaseMinutes;
    private int cooldownMinutes;
}
