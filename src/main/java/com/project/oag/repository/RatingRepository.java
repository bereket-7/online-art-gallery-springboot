package com.project.oag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	 //List<Rating> findByArtwork(Artwork artwork);
	 Optional<Rating> findByUserIdAndArtworkId(Long userId, Long artworkId);

	List<Rating> findByArtwork(Long artworkId);

}
