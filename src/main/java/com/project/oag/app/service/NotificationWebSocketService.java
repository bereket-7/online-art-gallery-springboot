package com.project.oag.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Broadcast a global notification to all connected clients.
     */
    public void broadcastNotification(String message) {
        log.info("Broadcasting websocket notification: {}", message);
        messagingTemplate.convertAndSend("/topic/global", message);
    }

    /**
     * Send a notification to a specific user queue (requires STOMP user mapping config).
     */
    public void sendUserNotification(String userEmail, String message) {
        log.info("Sending websocket notification to {}: {}", userEmail, message);
        // Note: Client should subscribe to /user/queue/notifications
        messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications", message);
    }
}
