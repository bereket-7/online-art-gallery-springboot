package com.project.oag.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua_parser.Parser;

@Configuration
public class LoginNotificationConfig {
    @Bean
    public Parser uaParser() throws IOException {
        return new Parser();
    }
}
