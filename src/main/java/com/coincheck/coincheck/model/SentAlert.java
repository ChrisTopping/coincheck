package com.coincheck.coincheck.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SentAlert {

    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private Coin coin;

    @ElementCollection
    private List<AlertType> alertTypes;

    @OneToOne
    private MarketCap marketCap;

    @CreatedDate
    private LocalDateTime sentDate;
}
