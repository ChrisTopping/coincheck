package com.coincheck.coincheck.configuration;

import com.coincheck.coincheck.model.Coin;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "email")
@Data
public class EmailProperties {
    private String from;
    private String password;
    private String to;
}
