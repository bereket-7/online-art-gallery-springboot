package com.project.oag.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageResponseDto {
    private Long id;
    private Long threadId;
    private Long senderId;
    private String senderName;
    private String body;
    private Timestamp createdAt;
    private boolean read;
}
