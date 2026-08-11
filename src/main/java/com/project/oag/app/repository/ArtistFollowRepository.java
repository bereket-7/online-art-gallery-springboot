package com.project.oag.app.repository;

import com.project.oag.app.entity.ArtistFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistFollowRepository extends JpaRepository<ArtistFollow, Long> {
    Optional<ArtistFollow> findByFollowerIdAndArtistId(Long followerId, Long artistId);

    boolean existsByFollowerIdAndArtistId(Long followerId, Long artistId);

    long countByArtistId(Long artistId);
}
