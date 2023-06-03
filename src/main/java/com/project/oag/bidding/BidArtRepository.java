package com.project.oag.bidding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.bidding.BidArt;

@Repository
public interface BidArtRepository extends JpaRepository<BidArt, Long> {
    //List<BidArt> findByStartingTimeLessThanEqualAndBiddingStartedFalse(LocalDateTime startingTime);

}
