package com.project.oag.config;

import com.project.oag.config.properties.FileStorageConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Getter
@Setter
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final FileStorageConfig fileStorageConfig;

    public WebConfig(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:".concat(fileStorageConfig.baseFolder()));
    }
}


