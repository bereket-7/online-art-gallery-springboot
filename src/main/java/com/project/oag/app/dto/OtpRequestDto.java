package com.project.oag.app.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OtpRequestDto {
    private Long userId;
    private String address;
    private String name;
    private OtpCodeTypeDto otpType;
    private NotificationType notificationType;
    private NotificationChannel notificationChannel;

}
