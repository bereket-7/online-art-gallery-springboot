package com.project.oag.app.service;

import com.project.oag.app.dto.AuctionStatus;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Auction;
import com.project.oag.app.entity.Bid;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.AuctionRepository;
import com.project.oag.app.repository.BidRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;

    public AuctionService(AuctionRepository auctionRepository,
                          BidRepository bidRepository,
                          ArtworkRepository artworkRepository,
                          UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Auction createAuction(Long artworkId, Timestamp startTime, Timestamp endTime, BigDecimal reservePrice) {
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        Auction auction = new Auction();
        auction.setArtwork(artwork);
        auction.setStartTime(startTime);
        auction.setEndTime(endTime);
        auction.setReservePrice(reservePrice);
        auction.setStatus(AuctionStatus.ACTIVE);
        return auctionRepository.save(auction);
    }

    @Transactional
    public Bid placeBid(Long auctionId, Long bidderId, BigDecimal amount) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new GeneralException("Auction is not active");
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.before(auction.getStartTime()) || now.after(auction.getEndTime())) {
            throw new GeneralException("Auction is not open for bidding");
        }
        if (auction.getCurrentBid() != null && amount.compareTo(auction.getCurrentBid()) <= 0) {
            throw new GeneralException("Bid must exceed current bid");
        }

        User bidder = userRepository.findById(bidderId)
                .orElseThrow(() -> new ResourceNotFoundException("Bidder not found"));

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(bidder);
        bid.setAmount(amount);
        Bid saved = bidRepository.save(bid);

        auction.setCurrentBid(amount);
        auctionRepository.save(auction);
        return saved;
    }

    public Auction getAuction(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
    }

    public List<Auction> getActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE);
    }

    public List<Bid> getBids(Long auctionId) {
        return bidRepository.findByAuctionIdOrderByAmountDesc(auctionId);
    }
}
