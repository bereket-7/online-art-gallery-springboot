package com.project.oag.config.properties;

import com.project.oag.common.AppConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "frontend-url-config")
@ConfigurationPropertiesScan
public record FrontendUrlConfig(
        @NotNull @Pattern(regexp = AppConstants.URL_PATTERN) String baseUrl,
        @NotNull @Pattern(regexp = AppConstants.URL_PATH_PATTERN) String returnUrl
) {
}
