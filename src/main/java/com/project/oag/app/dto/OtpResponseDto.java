package com.project.oag.app.dto;

import lombok.Builder;

@Builder
public record OtpResponseDto(
        int expireAfter,
        int allowResendAfter
) {
}
