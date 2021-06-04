package com.coincheck.coincheck.repository;

import com.coincheck.coincheck.model.AlertType;
import com.coincheck.coincheck.model.Coin;
import com.coincheck.coincheck.model.SentAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SentAlertRepository extends JpaRepository<SentAlert, UUID> {

    int countByCoinAndAlertTypesContainsAndSentDateAfter(Coin coin, AlertType alertType, LocalDateTime sentDate);

    void deleteBySentDateBefore(LocalDateTime sentDate);

}
