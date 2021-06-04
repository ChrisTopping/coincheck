package com.coincheck.coincheck.service;

import com.coincheck.coincheck.configuration.EmailProperties;
import com.coincheck.coincheck.model.Alert;
import com.coincheck.coincheck.service.email_contributor.EmailContributor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailProperties emailProperties;
    private final JavaMailSender javaMailSender;
    private final List<EmailContributor> contributors;
    private final DateTimeService dateTimeService;

    public void emailAlerts(List<Alert> alerts) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperties.getFrom());
        message.setTo(emailProperties.getTo());
        message.setSubject(buildSubject(alerts));
        message.setText(buildBody(alerts));
        javaMailSender.send(message);
    }

    private String buildSubject(List<Alert> alerts) {
        Alert alert = alerts.get(0);
        String coinName = alert.getMarketCap().getCoin().getName().toUpperCase();
        double marketCap = alert.getMarketCap().getMarketCapUsd().doubleValue();
        return String.format("%s Market Cap: $%,.0f (%d minutes ago)", coinName, marketCap, dateTimeService.minutesAgoUTC(alert.getMarketCap().getFromDateTime()));
    }

    private String buildBody(List<Alert> alerts) {
        Alert alert = alerts.get(0);
        String coinName = alert.getMarketCap().getCoin().getName().toUpperCase();
        double marketCap = alert.getMarketCap().getMarketCapUsd().doubleValue();

        String bodyFromContributors = getReasonsFromContributors(alerts);

        return String.format("%s market cap was $%,.0f %d minutes ago.\n\nYou have received this email because:\n\n%s", coinName, marketCap, dateTimeService.minutesAgoUTC(alert.getMarketCap().getFromDateTime()), bodyFromContributors);
    }

    private String getReasonsFromContributors(List<Alert> alerts) {
        return alerts.stream()
                .map(alert -> {
                    Optional<EmailContributor> contributor = contributors
                            .stream()
                            .filter(c -> c.canContribute(alert.getAlertType()))
                            .findFirst();

                    return contributor
                            .map(emailContributor -> "\t•\t" + emailContributor.contribute(alert))
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n\n"));
    }

}
