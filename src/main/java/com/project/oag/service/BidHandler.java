package com.project.oag.service;

import com.project.oag.entity.Bid;
import com.project.oag.entity.BidArt;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;

public class BidHandler extends TextWebSocketHandler {
   /* @Autowired
    private SimpMessagingTemplate messagingTemplate;
   
    @Autowired
    private BidService bidService;
    
    @MessageMapping("/bids")
    public void handleBid(@Payload Bid bid, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        bid = bidService.placeBid(bid);
        messagingTemplate.convertAndSend("/topic/bids/" + bid.getArtwork().getId(), bid);
    }
    */


    private boolean isBiddingOpen(BidArt artwork) {
        LocalDateTime bidEndTime = artwork.getBidEndTime();
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isBefore(bidEndTime);
    }


}
