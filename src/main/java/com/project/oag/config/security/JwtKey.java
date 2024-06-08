package com.project.oag.config.security;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "jwt-key")
public record JwtKey(
        @NotEmpty String secret,
        @Min(1) long expireAfter
) {
}
