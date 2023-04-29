package com.project.oag.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.BidArt;

@Repository
public interface BidArtRepository extends JpaRepository<BidArt, Long> {
    List<BidArt> findByStartingTimeLessThanEqualAndBiddingStartedFalse(LocalDateTime startingTime);

}
