package com.project.oag.bidding;

import com.project.oag.bidding.BidArt;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
public class BidHandler extends TextWebSocketHandler {
    private boolean isBiddingOpen(BidArt artwork) {
        LocalDateTime bidEndTime = artwork.getBidEndTime();
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isBefore(bidEndTime);
    }
}
