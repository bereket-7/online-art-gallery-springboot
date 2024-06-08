package com.project.oag.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyOtpRequestDTO {
    @JsonProperty("otp")
    private String otp;
    @JsonProperty("username")
    private String username;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("rememberMe")
    private boolean rememberMe;
    @JsonProperty("resendOtp")
    private boolean resendOtp;
    @JsonProperty("medium")
    private NotificationChannel medium;
}
