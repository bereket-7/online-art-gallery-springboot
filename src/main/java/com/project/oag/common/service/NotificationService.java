package com.project.oag.common.service;

import com.project.oag.app.dto.NotificationDto;

public interface NotificationService {
    boolean sendNotification(NotificationDto emailDto);
}
