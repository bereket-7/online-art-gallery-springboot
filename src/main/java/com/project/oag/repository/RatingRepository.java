package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Artwork;
import com.project.oag.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	 List<Rating> findByArtwork(Artwork artwork);
}
