package com.project.oag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.project.oag.entity.Artwork;
import com.project.oag.entity.Rating;
import com.project.oag.entity.User;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	 Double findAverageRatingByArtworkId(@Param("artworkId") Long artworkId);
	 
	 Rating findByUserAndArtwork(User user, Artwork artwork);

	Optional<Rating> findByUserIdAndArtworkId(String userIdAndArtworkId);

	List<Rating> findByArtwork(Artwork artwork);
}