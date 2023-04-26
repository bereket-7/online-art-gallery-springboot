package com.project.oag.utils;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.project.oag.entity.Bid;

public class BidHandler extends TextWebSocketHandler {
 
    private final SimpMessagingTemplate messagingTemplate;
 
    public BidHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
 
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the message and handle the bid
        Bid bid = // parse the bid from the message
        // Handle the bid, update the database, etc.
        // Notify other clients of the new bid
        messagingTemplate.convertAndSend("/topic/bids", bid);
    }
}
