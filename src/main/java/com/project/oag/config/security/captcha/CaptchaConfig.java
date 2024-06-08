package com.project.oag.config.security.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "google.recaptcha.key")
public record CaptchaConfig(
        String site,
        String secret,
        float threshold,
        String verifyUrl,
        int allowedAttempts) {
}
