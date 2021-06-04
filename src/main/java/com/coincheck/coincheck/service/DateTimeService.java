package com.coincheck.coincheck.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class DateTimeService {

    public long minutesAgoUTC(LocalDateTime localDateTime) {
        Duration age = Duration.between(localDateTime, LocalDateTime.now(ZoneOffset.UTC));
        return age.toMinutes();
    }

}
