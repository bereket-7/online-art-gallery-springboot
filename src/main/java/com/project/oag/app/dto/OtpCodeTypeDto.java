package com.project.oag.app.dto;

public enum OtpCodeTypeDto {
    ALL("all"),
    NUM("num");

    private final String type;

    OtpCodeTypeDto(String otpType) {
        this.type = otpType;
    }
}
