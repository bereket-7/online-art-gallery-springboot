package com.project.oag.app.service;

import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.dto.ReviewResponseDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Rating;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.RatingRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.BadRequestException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {

    private static final List<OrderStatus> PURCHASED_STATUSES = List.of(
            OrderStatus.CONFIRMED, OrderStatus.PROCESSING, OrderStatus.SHIPPED, OrderStatus.DELIVERED);

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final OrderRepository orderRepository;

    public RatingService(RatingRepository ratingRepository,
                         UserRepository userRepository,
                         ArtworkRepository artworkRepository,
                         OrderRepository orderRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public ReviewResponseDto rateArtwork(Long userId, Long artworkId, double ratingValue, String comment) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }
        assertPurchased(userId, artworkId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        Rating existing = ratingRepository.findByUserAndArtwork(user, artwork);
        if (existing != null) {
            existing.setRatingValue(ratingValue);
            if (comment != null) {
                existing.setComment(comment);
            }
            return CommerceMappers.toReviewDto(ratingRepository.save(existing));
        }

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setArtwork(artwork);
        rating.setRatingValue(ratingValue);
        rating.setComment(comment);
        return CommerceMappers.toReviewDto(ratingRepository.save(rating));
    }

    public List<ReviewResponseDto> getReviewsForArtwork(Long artworkId) {
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        return CommerceMappers.toReviewDtoList(ratingRepository.findByArtwork(artwork));
    }

    public List<Rating> getRatingsForArtwork(Long artworkId) {
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        return ratingRepository.findByArtwork(artwork);
    }

    public Double getAverageRating(Long artworkId) {
        return ratingRepository.findAverageRatingByArtworkId(artworkId);
    }

    private void assertPurchased(Long userId, Long artworkId) {
        if (!orderRepository.existsPurchasedArtwork(userId, artworkId, PURCHASED_STATUSES)) {
            throw new UserAuthorizationException("Reviews are limited to purchased artworks");
        }
    }
}
