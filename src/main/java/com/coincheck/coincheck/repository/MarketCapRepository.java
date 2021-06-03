package com.coincheck.coincheck.repository;

import com.coincheck.coincheck.model.Coin;
import com.coincheck.coincheck.model.MarketCap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MarketCapRepository extends JpaRepository<MarketCap, UUID> {

    List<MarketCap> findAllByCoinAndCreatedDateAfter(Coin coin, LocalDateTime createdDate);
}
