package com.coincheck.coincheck.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Coin {
    private String name;
    private String platformId;
    private String contractAddress;
    @Embedded
    private AlertConfiguration alertConfiguration;

}
