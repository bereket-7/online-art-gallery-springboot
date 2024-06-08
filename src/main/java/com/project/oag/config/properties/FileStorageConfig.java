package com.project.oag.config.properties;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.List;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "file-storage")
public record FileStorageConfig(
        @NotEmpty String baseFolder,
        @NotEmpty String image,
        @NotEmpty String doc,
        @NotEmpty List<String> imageAllowedTypes,
        @NotEmpty List<String> docAllowedTypes
) {
}
