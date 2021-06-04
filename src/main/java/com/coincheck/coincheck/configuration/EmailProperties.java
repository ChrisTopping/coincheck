package com.coincheck.coincheck.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "email")
@Data
public class EmailProperties {
    private String from;
    private String password;
    private String to;
}
