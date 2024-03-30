package com.project.oag.app.controller;

import com.project.oag.app.model.Rating;
import com.project.oag.app.service.RatingService;
import com.project.oag.app.model.User;
import com.project.oag.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.exceptions.ArtworkNotFoundException;
import com.project.oag.exceptions.RatingAlreadyExistsException;
import com.project.oag.exceptions.UserNotFoundException;

@RestController
@RequestMapping("rating/artworks")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{artworkId}/rate")
    public ResponseEntity<?> addRating(@PathVariable Long artworkId, @RequestBody Rating rating) {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.getUsername();
        User user = userRepository.findByUsername(username);
            Long userId = user.getId();
            ratingService.addRating(userId, artworkId, rating.getRating());
            return ResponseEntity.ok().build();
        } catch (ArtworkNotFoundException | UserNotFoundException | RatingAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{artworkId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForArtwork(@PathVariable Long artworkId) {
        try {
            double averageRating = ratingService.getAverageRatingForArtwork(artworkId);
            return ResponseEntity.ok().body(averageRating);
        } catch (ArtworkNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

