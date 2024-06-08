package com.project.oag.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "email-config")
public record EmailConfig(
        String from,
        String verifyEmailSubject,
        String verifyEmailContent,
        String accountVerifiedSubject,
        String accountVerifiedContent,
        String passwordForgotSubject,
        String passwordForgotContent,
        String emailChangeInitiate,
        String phoneChangeInitiate,
        String resetSuccessSubject,
        String resetSuccessContent,
        String appName
) {

}
