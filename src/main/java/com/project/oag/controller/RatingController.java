package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Artwork;
import com.project.oag.entity.Rating;
import com.project.oag.service.ArtworkService;
import com.project.oag.service.RatingService;

@RestController
@RequestMapping("/ratings")
public class RatingController {
	 @Autowired
	 private RatingService ratingService;
	 
	 @Autowired
	 private ArtworkService artworkService;
	 
	 	@PostMapping
	    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
	        Rating savedRating = ratingService.save(rating);
	        return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
	    }

	    @GetMapping("/{artworkId}/average")
	    public ResponseEntity<Double> getAverageRating(@PathVariable Long artworkId) {
	        Artwork artwork = artworkService.findById(artworkId);
	        Double averageRating = ratingService.getAverageRating(artwork);
	        return new ResponseEntity<>(averageRating, HttpStatus.OK);
	    }
	 /*
	   @PostMapping("/artworks/{artworkId}/ratings")
	    public ResponseEntity<?> rateArtwork(@PathVariable Long artworkId,
	                                                 @RequestParam int stars) {
	        RatingDto ratingDto = ratingService.rateArtwork(artworkId, stars);
	        return ResponseEntity.status(HttpStatus.CREATED).body(ratingDto);
	    }
	    @GetMapping("/artworks/{artworkId}/average-rating")
	    public ResponseEntity<?> getAverageRating(@PathVariable Long artworkId) {
	        double averageRating = ratingService.getAverageRating(artworkId);
	        return ResponseEntity.ok(averageRating);
	    }*/
	 /**
	   @PostMapping
	    public void addRating(@PathVariable String artworkId, @RequestBody Rating rating) {
	        rating.setArtworkId(artworkId);
	        ratingService.addRating(rating);
	    }
	    
	    @GetMapping
	    public List getRatingsForArtwork(@PathVariable String artworkId) {
	        return ratingService.getRatingsForArtwork(artworkId);
	    }
	    
	    @GetMapping("/avg")
	    public double getAvgRatingForArtwork(@PathVariable String artworkId) {
	        return ratingService.getAvgRatingForArtwork(artworkId);
	    }**/
	
}
