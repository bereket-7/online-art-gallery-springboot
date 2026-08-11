package com.project.oag.app.service;

import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Rating;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.RatingRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    public RatingService(RatingRepository ratingRepository,
                         UserRepository userRepository,
                         ArtworkRepository artworkRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    @Transactional
    public Rating rateArtwork(Long userId, Long artworkId, double ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new GeneralException("Rating must be between 1 and 5");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        Rating existing = ratingRepository.findByUserAndArtwork(user, artwork);
        if (existing != null) {
            existing.setRatingValue(ratingValue);
            return ratingRepository.save(existing);
        }

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setArtwork(artwork);
        rating.setRatingValue(ratingValue);
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForArtwork(Long artworkId) {
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        return ratingRepository.findByArtwork(artwork);
    }

    public Double getAverageRating(Long artworkId) {
        return ratingRepository.findAverageRatingByArtworkId(artworkId);
    }
}
