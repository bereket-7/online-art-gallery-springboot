package com.project.oag.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "password-config")
public record PasswordConfig(
        int requiredLength,
        int forgotLength,
        int resetLength,
        int forgotRetryLimit,
        int resetRetryLimit
) {
}