package com.project.oag.app.service;

import com.project.oag.app.model.Artwork;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.RatingRepository;
import com.project.oag.app.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.user.User;
import com.project.oag.exceptions.ArtworkNotFoundException;
import com.project.oag.exceptions.RatingAlreadyExistsException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.user.UserRepository;

@Service
public class RatingService {
	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired
	private ArtworkRepository artworkRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void addRating(Long userId, Long artworkId, int rating) throws ArtworkNotFoundException, UserNotFoundException, RatingAlreadyExistsException {
	    Artwork artwork = artworkRepository.findById(artworkId).orElse(null);
	    if (artwork == null) {
	        throw new ArtworkNotFoundException("Artwork not found with ID: " + artworkId);
	    }

	    User user = userRepository.findById(userId).orElse(null);
	    if (user == null) {
	        throw new UserNotFoundException("User not found with ID: " + userId);
	    }

	    Rating existingRating = ratingRepository.findByUserAndArtwork(user, artwork);
	    if (existingRating != null) {
	    	throw new RatingAlreadyExistsException("User " + user.getUsername() + " has already rated artwork " + artwork.getArtworkName());
	        //throw new RatingAlreadyExistsException("User " + userId + " has already rated artwork " + artworkId);
	    }

	    Rating newRating = new Rating();
	    newRating.setUser(user);
	    newRating.setArtwork(artwork);
	    newRating.setRating(rating);
	    ratingRepository.save(newRating);
	}
	
	public double getAverageRatingForArtwork(Long artworkId) throws ArtworkNotFoundException {
	    Artwork artwork = artworkRepository.findById(artworkId).orElse(null);
	    if (artwork == null) {
	        throw new ArtworkNotFoundException("Artwork not found with ID: " + artworkId);
	    }
	    return artwork.getAverageRating();
	}

}
