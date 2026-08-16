package com.project.oag.app.repository;

import com.project.oag.app.entity.AuctionWatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AuctionWatchRepository extends JpaRepository<AuctionWatch, Long> {
    Optional<AuctionWatch> findByAuctionIdAndUserId(Long auctionId, Long userId);

    boolean existsByAuctionIdAndUserId(Long auctionId, Long userId);

    @Transactional
    void deleteByAuctionIdAndUserId(Long auctionId, Long userId);
}
