package com.project.oag.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bids")
public class BidController {
    
    //@Autowired
    //private BidArtRepository bidArtRepository;
    
    //@Autowired
    //private BidRepository bidRepository;
/**
 @PostMapping("/{artworkId}")
 public ResponseEntity<Bid> placeBid(@PathVariable Long artId, 
   @RequestBody BigDecimal amount) {
  
  Optional<BidArt> optionalBidArt = bidArtRepository.findById(artId);
  
  if (!optionalBidArt.isPresent()) {
   return ResponseEntity.notFound().build();
  }
  
  BidArt bidArt = optionalBidArt.get();
  
  LocalDateTime now = LocalDateTime.now();
  
  if (now.isBefore(bidArt.getBiddingStartTime()) || now.isAfter(bidArt.getBiddingEndTime())) {
   //return ResponseEntity.badRequest().body("Bidding is not open for this artwork");
   	return new ResponseEntity<>(new Bid(false, "Bidding is not open for this artwork"), HttpStatus.UNAUTHORIZED);
  }
 
        if (amount.compareTo(bidArt.getInitialAmount()) <= 0) {
            //return ResponseEntity.badRequest().body("Bid amount should be greater than the initial amount");
            return new ResponseEntity<>(new Bid(false, "Bid amount should be greater than the initial amount\""), HttpStatus.UNAUTHORIZED);
        }
       
        Optional<Bid> highestBidOptional = bidRepository.findFirstByArtworkOrderByAmountDesc(bidArt);
        
        if (highestBidOptional.isPresent() && amount.compareTo(highestBidOptional.get().getAmount()) <= 0) {
           // return ResponseEntity.badRequest().body("Bid amount should be greater than the current highest bid");
            return new ResponseEntity<>(new Bid(false, "Bid amount should be greater than the current highest bid \""), HttpStatus.UNAUTHORIZED);
        }
        
        Bid bid = new Bid();
        bid.setBidArt(bidArt);
        bid.setAmount(amount);
        
        bidRepository.save(bid);
        
        return ResponseEntity.ok(bid);
 }*/
}