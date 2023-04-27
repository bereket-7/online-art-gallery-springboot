package com.project.oag.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.common.FileUploadUtil;
import com.project.oag.entity.Bid;
import com.project.oag.entity.BidArt;
import com.project.oag.service.BidArtService;
import com.project.oag.service.BidService;

@RestController
@RequestMapping("/bid")
@CrossOrigin("http://localhost:8080/")
public class BidController {
	/*
	private String path = "src/main/resources/static/img/bid-images/";
	@Autowired
	 private final BidService bidService;
	    @Autowired
	    private BidArtService bidArtService;


    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }
    
    @PostMapping("/")
    public void saveBid(@RequestBody Bid bid) {
        bidService.saveBid(bid);
    }
    @PostMapping("/bids")
    public Bid createBid(@RequestBody Bid bid) {
        return bidService.createBid(bid);
    }

    @GetMapping("/bids/{id}")
    public Bid getBid(@PathVariable Long id) {
        return bidService.getBid(id);
    }

    @GetMapping("/bids")
    public List<Bid> getAllBids() {
        return bidService.getAllBids();
    }

    @PutMapping("/bids/{id}")
    public Bid updateBid(@RequestBody Bid bid, @PathVariable Long id) {
        return bidService.updateBid(bid, id);
    }

    @DeleteMapping("/bids/{id}")
    public void deleteBid(@PathVariable Long id) {
        bidService.deleteBid(id);
    }

    // CRUD operations for BidArt
    @PostMapping("/bidArt/upload")
    public BidArt createBidArt(@ModelAttribute("bidArt") BidArt bidArt,@RequestParam("image") MultipartFile image) throws IOException{
    	 String filename = StringUtils.cleanPath(image.getOriginalFilename());
			bidArt.setArtworkPhoto(filename);
        FileUploadUtil.uploadFile(path, filename, image); 
        return bidArtService.createBidArt(bidArt);
        // return new ResponseEntity<>(new BidArt(filename,"Artwork is successfully submitted,it will display in the gallery after approval thank you"),HttpStatus.OK);	
    }

    @GetMapping("/bidArts/{id}")
    public BidArt getBidArt(@PathVariable Long id) {
        return bidArtService.getBidArt(id);
    }

    @GetMapping("/bidArts")
    public List<BidArt> getAllBidArts() {
        return bidArtService.getAllBidArts();
    }

    @PutMapping("/bidArts/{id}")
    public BidArt updateBidArt(@RequestBody BidArt bidArt, @PathVariable Long id) {
        return bidArtService.updateBidArt(bidArt, id);
    }

    @DeleteMapping("/bidArts/{id}")
    public void deleteBidArt(@PathVariable Long id) {
        bidArtService.deleteBidArt(id);
    }
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