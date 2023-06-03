package com.project.oag.service;

import com.project.oag.entity.BidArt;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
public class BidHandler extends TextWebSocketHandler {
    private boolean isBiddingOpen(BidArt artwork) {
        LocalDateTime bidEndTime = artwork.getBidEndTime();
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isBefore(bidEndTime);
    }
}
