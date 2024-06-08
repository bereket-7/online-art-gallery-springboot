package com.project.oag.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "mailer-config")
public record MailerConfig(
        String protocol,
        String host,
        String port,
        String username,
        String password,
        boolean smtpAuth,
        boolean smtpStarttlsEnable,
        boolean sslEnable
) {
}
