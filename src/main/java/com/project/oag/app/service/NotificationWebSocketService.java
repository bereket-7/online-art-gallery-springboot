package com.project.oag.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastNotification(String message) {
        log.info("Broadcasting websocket notification: {}", message);
        messagingTemplate.convertAndSend("/topic/global", payload(message, "GLOBAL"));
    }

    public void sendUserNotification(String userEmail, String message) {
        sendUserNotification(userEmail, message, "INFO");
    }

    public void sendUserNotification(String userEmail, String message, String type) {
        log.info("Sending websocket notification to {}: {}", userEmail, message);
        messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications", payload(message, type));
    }

    private Map<String, String> payload(String message, String type) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("type", type);
        body.put("createdAt", Instant.now().toString());
        return body;
    }
}
