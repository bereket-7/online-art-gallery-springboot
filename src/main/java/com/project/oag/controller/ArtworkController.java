package com.project.oag.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.project.oag.controller.dto.ArtworkDto;
import com.project.oag.entity.Artwork;
import com.project.oag.service.ArtworkService;
import com.project.oag.service.UserService;

@RestController
@RequestMapping("/artwork")
@CrossOrigin("http://localhost:8080/")
public class ArtworkController {
	private String path = "src/main/resources/static/img/artwork-images/";
	
	@Autowired
	private ArtworkService artworkService;
	
	@Autowired
	private UserService userService;
	
	public ArtworkController(ArtworkService artworkService) {
		super();
		this.artworkService = artworkService;
	}
	
	 @PostMapping("/upload")
	 public ResponseEntity<Artwork> artworkUpload(@ModelAttribute("artwork") Artwork artwork,@RequestParam("image") MultipartFile image) throws IOException{
		  String filename = StringUtils.cleanPath(image.getOriginalFilename());
			artwork.setArtworkPhoto(filename);
			artworkService.saveArtwork(artwork);
			FileUploadUtil.uploadFile(path, filename, image); 	
			return new ResponseEntity<>(new Artwork(filename,"Artwork is successfully submitted,it will display in the gallery after approval thank you"),HttpStatus.OK);	
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

	   @GetMapping("/byArtistName/{artistName}") 
	   public List<Artwork> getPhotosByArtistName(@PathVariable String artistName) {       
		   return artworkService.getArtworksByArtistName(artistName);    
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
	    
	    @PutMapping("/{id}")
	    public ResponseEntity<ArtworkDto> updateArtwork(@PathVariable Long id, @RequestBody ArtworkDto artworkDto) {
	        ArtworkDto updatedArtworkDto = artworkService.updateArtwork(id, artworkDto);
	        return ResponseEntity.ok(updatedArtworkDto);
	    }
	    
	    @GetMapping("/recent")
	    public ResponseEntity<List<Artwork>> getRecentArtworks() {
	        List<Artwork> artworks = artworkService.getRecentArtworks();
	        return new ResponseEntity<>(artworks, HttpStatus.OK);
	    }
	   /* 
	    @GetMapping("/photo-category-price-size")
	    public List<ArtworkDto> getArtworkPhotoAndCategoryAndPriceAndSize() {
	        return artworkService.getArtworkPhotoAndCategoryAndPriceAndSize();
	    }
	  
	    @GetMapping("/{artworkId}/average-rating")
	    public double getAverageRating(@PathVariable Long artworkId) {
	        return artworkService.getAverageRating(artworkId);
	    }*/
		
}
           

