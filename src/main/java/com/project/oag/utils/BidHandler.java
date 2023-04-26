package com.project.oag.utils;

import java.math.BigDecimal;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.project.oag.entity.Bid;
import com.project.oag.service.BidArtService;
import com.project.oag.service.BidService;

public class BidHandler extends TextWebSocketHandler {

    private BidArtService bidArtService;
    private BidService bidService;
 
    private final SimpMessagingTemplate messagingTemplate;
 
    public BidHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the message and handle the bid
        Bid bid = // parse the bid from the message
        BidArt bidArt = bidArtService.getBidArtById(bid.getArtwork().getId());
        BigDecimal currentHighestBidAmount = bidService.getCurrentHighestBidAmount(bidArt);
        if (bid.getAmount().compareTo(currentHighestBidAmount) <= 0) {
            // Reject the bid
        } else {
            // Handle the bid, update the database, etc.
            // Notify other clients of the new bid
            messagingTemplate.convertAndSend("/topic/bids", bid);
        }
    }
 
}
