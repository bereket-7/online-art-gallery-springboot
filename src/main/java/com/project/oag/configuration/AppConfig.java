package com.project.oag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.oag.security.ActiveUserStore;

@Configuration
public class AppConfig {
    // beans

    @Bean
    public ActiveUserStore activeUserStore() {
        return new ActiveUserStore();
    }

}