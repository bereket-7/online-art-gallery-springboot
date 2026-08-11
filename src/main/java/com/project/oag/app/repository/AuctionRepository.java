package com.project.oag.app.repository;

import com.project.oag.app.dto.AuctionStatus;
import com.project.oag.app.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatus(AuctionStatus status);

    List<Auction> findByArtworkId(Long artworkId);
}
