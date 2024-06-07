package com.project.oag.app.repository;

import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Rating;
import com.project.oag.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Double findAverageRatingByArtworkId(@Param("artworkId") Long artworkId);

    Rating findByUserAndArtwork(User user, Artwork artwork);

    List<Rating> findByArtwork(Artwork artwork);
}