package com.project.oag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the application.
 */
@Configuration
public class CorsConfig {

    /**
     * Creates a new {@link CorsFilter} bean that can be used to enable CORS for all requests.
     *
     * @return a new {@link CorsFilter} bean
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:8080");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(source);
    }
}
