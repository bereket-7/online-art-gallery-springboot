package com.project.oag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.project.oag.entity.Bid;

public class BidHandler extends TextWebSocketHandler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private BidService bidService;
    
    @MessageMapping("/bids")
    public void handleBid(@Payload Bid bid, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        bid = bidService.placeBid(bid);
        messagingTemplate.convertAndSend("/topic/bids/" + bid.getArtwork().getId(), bid);
    }
    
}
