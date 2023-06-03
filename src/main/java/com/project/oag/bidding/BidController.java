package com.project.oag.bidding;
import com.project.oag.entity.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/bid")
@CrossOrigin("http://localhost:8080/")
public class BidController {
    @Value("${uploadDir}")
    private String uploadFolder;
    @Autowired
    private BidArtService bidArtService;
    @Autowired
    private BidService bidService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/saveBidArt")
    public @ResponseBody ResponseEntity<?> createBidArt(@RequestParam("title") String title,
                                                       @RequestParam("initialAmount") BigDecimal initialAmount,
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
    @GetMapping("/{id}")
    public ResponseEntity<BidArt> getBidArt(@PathVariable Long id, Model model) {
        Optional<BidArt> bidArt = bidArtService.getBidArtById(id);

        if (bidArt == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(bidArt.get(), HttpStatus.OK);
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getBidArtImage(@PathVariable Long id, Model model) {
        Optional<BidArt> bidArt = bidArtService.getBidArtById(id);
        if (bidArt == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        byte[] imageBytes = bidArt.get().getImage();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<BidArt>> getAllBidArt() {
        List<BidArt> bidArtList = bidArtService.getAllBidArts();

        if (bidArtList == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(bidArtList, HttpStatus.OK);
    }
    @PostMapping("/placeBid")
    public ResponseEntity<String> placeBid(@RequestBody BidRequest bidRequest) {
        try {
            bidService.placeBid(bidRequest.getArtworkId(), bidRequest.getUserId(), bidRequest.getAmount());
            return ResponseEntity.ok("Bid placed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}