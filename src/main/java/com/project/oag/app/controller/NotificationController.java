package com.project.oag.app.controller;

import com.project.oag.app.service.NotificationService;
import com.project.oag.security.service.CustomUserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final CustomUserDetailsService userService;

    public NotificationController(NotificationService notificationService, CustomUserDetailsService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }
}


