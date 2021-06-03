package com.coincheck.coincheck.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.coincheck.coincheck.repository")
public class DatabaseConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("auditor");
    }
}
