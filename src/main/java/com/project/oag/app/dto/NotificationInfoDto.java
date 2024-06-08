package com.project.oag.app.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class NotificationInfoDto {
    private String name;
    private String otp;
    private NotificationType notificationType;
}
