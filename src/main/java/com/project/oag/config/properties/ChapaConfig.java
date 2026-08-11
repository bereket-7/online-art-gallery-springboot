package com.project.oag.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "chapa")
public class ChapaConfig {
    private String secretKey;
    private String returnUrlBase = "http://localhost:8088";

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getReturnUrlBase() {
        return returnUrlBase;
    }

    public void setReturnUrlBase(String returnUrlBase) {
        this.returnUrlBase = returnUrlBase;
    }
}
