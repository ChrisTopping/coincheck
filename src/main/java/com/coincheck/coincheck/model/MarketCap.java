package com.coincheck.coincheck.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MarketCap {

    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private Coin coin;
    private BigDecimal marketCapUsd;

    @CreatedDate
    private LocalDateTime createdDate;
}
