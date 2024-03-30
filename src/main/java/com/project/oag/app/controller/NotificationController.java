package com.project.oag.app.controller;

import com.project.oag.app.model.Notification;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.app.service.NotificationService;
import com.project.oag.security.service.CustomUserDetailsService;
import com.project.oag.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("http://localhost:8080/")
public class NotificationController {
    @Autowired
    private CustomUserDetailsService userService;
    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(
            @RequestParam("message") String message,
            @RequestParam("targetEmail") String targetEmail) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User adminUser = (User) authentication.getPrincipal();
            User targetedUser = userService.getUserByEmail(targetEmail);
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUser(targetedUser);
            notification.setRead(false);
            notification.setCreatedDateTime(LocalDateTime.now());
            notificationService.saveNotification(notification);
            return ResponseEntity.ok("Notification sent successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Targeted user not found!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification!");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Notification>> getUserNotifications(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<Notification> notifications = notificationService.getNotificationsForUser(user);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read.");
    }
}


