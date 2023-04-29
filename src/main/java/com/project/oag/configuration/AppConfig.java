package com.project.oag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.oag.security.ActiveUserStore;
@Configuration
public class AppConfig {

    @Bean
    public ActiveUserStore activeUserStore() {
        return new ActiveUserStore();
    }
    

        
        //@Value("${sendinblue.api.key}")
        //private String apiKey;
        /*
        @Bean
        public SendinblueClient sendinblueClient() {
            return new SendinblueClientBuilder()
                .apiKey(apiKey)
                .build();
        }*/
        

}