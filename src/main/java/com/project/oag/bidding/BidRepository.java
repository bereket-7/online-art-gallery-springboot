package com.project.oag.bidding;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.bidding.Bid;
import com.project.oag.bidding.BidArt;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
	//Optional<Bid> findFirstByArtworkOrderByAmountDesc(BidArt bidArt);
	List<Bid> findByArtworkOrderByAmountDescTimestampAsc(BidArt artwork);
}