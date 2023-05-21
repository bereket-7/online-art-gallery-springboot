package com.project.oag.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.entity.Artwork;
import com.project.oag.entity.Event;
import com.project.oag.service.ArtworkService;
import com.project.oag.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/artworks")
@CrossOrigin("http://localhost:8080/")
public class ArtworkController {
	//private String path = "src/main/resources/static/img/artwork-images/";
	
	 @Value("${uploadDir}")
	 private String uploadFolder;
	 private final Logger log = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private ArtworkService artworkService;

	@Autowired
	private UserService userService;	

	public ArtworkController(ArtworkService artworkService) {
		super();
		this.artworkService = artworkService;
	}
	
	 
	 @PostMapping("/saveArtwork")
	 public @ResponseBody ResponseEntity<?> registerArtwork(@RequestParam("artworkName") String artworkName,
	         @RequestParam("price") int price, @RequestParam("size") String size,
	         @RequestParam("artworkDescription") String artworkDescription, @RequestParam("artworkCategory") String artworkCategory, Model model, HttpServletRequest request,
	         final @RequestParam("image") MultipartFile file) {
	     try {
	         String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
	         log.info("uploadDirectory:: " + uploadDirectory);
	         String fileName = file.getOriginalFilename();
	         String filePath = Paths.get(uploadDirectory, fileName).toString();
	         log.info("FileName: " + file.getOriginalFilename());
	         if (fileName == null || fileName.contains("..")) {
	             model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence " + fileName);
	             return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName,
	                     HttpStatus.BAD_REQUEST);
	         }
	         String[] names = artworkName.split(",");
	         String[] descriptions = artworkDescription.split(",");
	         Date createDate = new Date();
	         log.info("artworkName: " + names[0] + " " + filePath);
	         log.info("artworkDescription: " + descriptions[0]);
	         log.info("Price: " + price);
	         try {
	             File dir = new File(uploadDirectory);
	             if (!dir.exists()) {
	                 log.info("Folder Created");
	                 dir.mkdirs();
	             }
	             // Save the file locally
	             BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
	             stream.write(file.getBytes());
	             stream.close();
	         } catch (Exception e) {
	             log.info("in catch");
	             e.printStackTrace();
	         }
	         byte[] imageData = file.getBytes();
	         Artwork artwork = new Artwork();
	         artwork.setArtworkName(names[0]);
	         artwork.setImage(imageData);
	         artwork.setPrice(price);
	         artwork.setSize(size);
	         artwork.setArtworkCategory(artworkCategory);
	         artwork.setStatus("pending");
	         artwork.setArtworkDescription(descriptions[0]);
	         artwork.setCreateDate(createDate);
	         artworkService.saveArtwork(artwork);
	         log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
	         return new ResponseEntity<>("Artwork Saved With File - " + fileName, HttpStatus.OK);
	     } catch (Exception e) {
	         e.printStackTrace();
	         log.info("Exception: " + e);
	         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	     }
	 }
	 @GetMapping("/{id}")
	 public ResponseEntity<Artwork> getArtwork(@PathVariable Long id, Model model) {
	     Optional<Artwork> artwork = artworkService.getArtworkById(id);

	     if (artwork == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }

    return new ResponseEntity<>(artwork.get(), HttpStatus.OK);
	 }
	 
	 
	 @GetMapping("/{id}/image")
	 public ResponseEntity<byte[]> getArtworkImage(@PathVariable Long id, Model model) {
	     Optional<Artwork> artwork = artworkService.getArtworkById(id);
	     if (artwork == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }
	     byte[] imageBytes = artwork.get().getImage();
	 
	     HttpHeaders headers = new HttpHeaders();
   		headers.setContentType(MediaType.IMAGE_PNG);
    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	 }
	 
	 
	 @GetMapping
	 public ResponseEntity<List<Artwork>> getAllArtwork() {
	     List<Artwork> artworkList = artworkService.getAllArtworks();

	     if (artworkList == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }

    return new ResponseEntity<>(artworkList, HttpStatus.OK);
	 }
	 
	 
	 

	   @GetMapping("/all")
	   public List<Artwork> getAllPhotos() { 
	       return artworkService.getAllArtworks(); 
	   }
	   
	   @DeleteMapping("/{id}") 
	   public void deleteArtwork(@PathVariable Long id) { 
		   artworkService.deleteArtwork(id); 
	   }
	   @GetMapping("/byCategory/{artworkCategory}") 
	   public List<Artwork> getArtworkByCategory(@PathVariable String artworkCategory) { 
	       return artworkService.getArtworkByCategory(artworkCategory); 
	   }
	   
	   @GetMapping("/byArtistId/{artistId}")      
	   public List<Artwork> getPhotosByArtworkName(@PathVariable int artistId) {        
		   return artworkService.getArtworksByArtistId(artistId);      
	  }
	    @GetMapping("/priceRange")
	    public List<Artwork> getArtworkByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
	        return artworkService.getArtworkByPriceRange(minPrice, maxPrice);
	    }

	    @GetMapping("/pending")
	    public List<Artwork> getPendingArtworks() {
	        return artworkService.getPendingArtworks();
	    }

	    @GetMapping("/accepted")
	    public List<Artwork> getAcceptedArtworks() {
	        return artworkService.getAcceptedArtworks();
	    }

	    @GetMapping("/rejected")
	    public List<Artwork> getRejectedArtworks() {
	        return artworkService.getRejectedArtworks();
	    }

	    @PutMapping("/{id}/accept")
	    public ResponseEntity<String> acceptArtwork(@PathVariable Long id) {
	        boolean accepted = artworkService.acceptArtwork(id);
	        if (accepted) {
	            return ResponseEntity.ok("Artwork with ID " + id + " has been accepted");
	        } else {
	            return ResponseEntity.badRequest().body("Artwork with ID " + id + " was not found or is not pending");
	        }
	    }

	    @PutMapping("/{id}/reject")
	    public ResponseEntity<String> rejectArtwork(@PathVariable Long id) {
	        boolean rejected = artworkService.rejectArtwork(id);
	        if (rejected) {
	            return ResponseEntity.ok("Artwork with ID " + id + " has been Rejected due to the reason it does not satisfy company standard");
	        } else {
	            return ResponseEntity.badRequest().body("Artwork with ID " + id + " was not found or is not in pending status");
	        }
	    }

	    @GetMapping("/recent")
	    public ResponseEntity<List<Artwork>> getRecentArtworks() {
	        List<Artwork> artworks = artworkService.getRecentArtworks();
	        return new ResponseEntity<>(artworks, HttpStatus.OK);
	    }
	   /*
	  
	    @GetMapping("/{artworkId}/average-rating")
	    public double getAverageRating(@PathVariable Long artworkId) {
	        return artworkService.getAverageRating(artworkId);
	    }*/

}
