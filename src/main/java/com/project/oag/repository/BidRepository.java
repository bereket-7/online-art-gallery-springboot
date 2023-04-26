package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Bid;
import com.project.oag.entity.BidArt;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
	//Optional<Bid> findFirstByArtworkOrderByAmountDesc(BidArt bidArt);
	List<Bid> findByArtworkOrderByAmountDescTimestampAsc(BidArt artwork);
}