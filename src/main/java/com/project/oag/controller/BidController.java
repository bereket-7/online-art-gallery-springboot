package com.project.oag.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Bid;
import com.project.oag.service.BidService;

@RestController
@RequestMapping("/bids")
public class BidController {
	@Autowired
	 private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }
    
    @GetMapping("/")
    public List<Bid> getAllBids() {
        return bidService.getAllBids();
    }

    @GetMapping("/{id}")
    public Bid getBidById(@PathVariable Long id) {
        Optional<Bid> bid = bidService.getBidById(id);
        return bid.orElse(null);
    }
    @PostMapping("/")
    public void saveBid(@RequestBody Bid bid) {
        bidService.saveBid(bid);
    }
    
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