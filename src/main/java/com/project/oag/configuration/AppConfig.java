package com.project.oag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.oag.security.ActiveUserStore;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public ActiveUserStore activeUserStore() {
        return new ActiveUserStore();
    }
}