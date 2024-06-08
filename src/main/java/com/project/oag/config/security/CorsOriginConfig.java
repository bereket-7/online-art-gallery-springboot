package com.project.oag.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.List;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "cors")
public record CorsOriginConfig(
        List<String> allowedMethods,
        List<String> listOfOrigins,
        List<String> exposedHeaders,
        List<String> allowedHeaders
) {
}