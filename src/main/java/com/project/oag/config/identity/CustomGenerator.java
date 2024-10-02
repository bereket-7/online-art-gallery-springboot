package com.project.oag.config.identity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomGenerator {

    @Bean(name = "customIdGenerator")
    public CustomIdGenerator customIdGenerator() {
        return new CustomIdGenerator();
    }
}