package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Bid;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
	//Optional<Bid> findFirstByArtworkOrderByAmountDesc(BidArt bidArt);
}