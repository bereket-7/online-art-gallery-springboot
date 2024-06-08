package com.project.oag.config.properties;

import com.project.oag.app.dto.OtpCodeTypeDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "otp-config")
public record OtpConfig(
        int expireAfter,
        int allowResendAfter,
        int retry,
        int length,
        OtpCodeTypeDto type,
        String salt
) {
}