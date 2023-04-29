package com.project.oag.service;

import org.springframework.web.socket.handler.TextWebSocketHandler;

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
}
