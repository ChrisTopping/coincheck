package com.coincheck.coincheck;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class CoincheckApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CoincheckApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
