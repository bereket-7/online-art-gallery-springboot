package com.project.oag.controller;
import com.project.oag.entity.BidArt;
import com.project.oag.entity.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/bid")
@CrossOrigin("http://localhost:8080/")
public class BidController {
    @Value("${uploadDir}")
    private String uploadFolder;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/saveBidArt")
    public @ResponseBody ResponseEntity<?> createBidArt(@RequestParam("title") String title,
                                                       @RequestParam("initialAmount") double initialAmount,
                                                        @RequestParam("artist") String artist,
                                                        @RequestParam("description") String description,
                                                        @RequestParam("bidEndTime") LocalDateTime  bidEndTime,
                                                        @RequestParam("startingTime") LocalDateTime startingTime,
                                                        Model model, HttpServletRequest request,
                                                        final @RequestParam("image") MultipartFile file) {
        try {
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            log.info("uploadDirectory:: " + uploadDirectory);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            log.info("FileName: " + file.getOriginalFilename());
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            String[] names =title.split(",");
            String[] descriptions = description.split(",");
            Date createDate = new Date();
            log.info("title: " + names[0]+" "+filePath);
            log.info("description: " + descriptions[0]);
            log.info("initialAmount: " + initialAmount);
            log.info("bidEndTime:" + bidEndTime);
            log.info("staringTime:" + startingTime);
            try {
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    log.info("Folder Created");
                    dir.mkdirs();
                }
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                log.info("in catch");
                e.printStackTrace();
            }
            byte[] imageData = file.getBytes();
            BidArt art = new BidArt();
            art.setTitle(names[0]);
            art.setInitialAmount(initialAmount);
            art.setDescription(descriptions[0]);
            art.setStartingTime(startingTime);
            art.setImage(imageData);
            art.setBidEndTime(bidEndTime);
            log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
            return new ResponseEntity<>("Bid Art Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
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