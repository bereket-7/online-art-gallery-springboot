package com.project.oag.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Artwork;
import com.project.oag.entity.Rating;
import com.project.oag.entity.User;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
	 Double findAverageRatingByArtworkId(@Param("artworkId") Long artworkId);
	 Rating findByUserAndArtwork(User user, Artwork artwork);
	 List<Rating> findByArtwork(Artwork artwork);
}