package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.exceptions.ArtworkNotFoundException;
import com.project.oag.exceptions.RatingAlreadyExistsException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.service.RatingService;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/rate")
    public ResponseEntity<?> addRating(@RequestParam Long userId, @RequestParam Long artworkId, @RequestParam int rating) {
        try {
            ratingService.addRating(userId, artworkId, rating);
            return ResponseEntity.ok().build();
        } catch (ArtworkNotFoundException | UserNotFoundException | RatingAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{artworkId}/average")
    public ResponseEntity<Double> getAverageRatingForArtwork(@PathVariable Long artworkId) {
        try {
            double averageRating = ratingService.getAverageRatingForArtwork(artworkId);
            return ResponseEntity.ok().body(averageRating);
        } catch (ArtworkNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
