package com.project.oag.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageThreadResponseDto {
    private Long threadId;
    private Long artworkId;
    private Long artistId;
    private Long buyerId;
    private long unread;
    private MessageResponseDto lastMessage;
    private List<MessageResponseDto> messages;
}
