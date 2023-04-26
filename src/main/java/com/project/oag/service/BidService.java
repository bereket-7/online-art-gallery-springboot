package com.project.oag.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.project.oag.entity.Bid;
import com.project.oag.entity.BidArt;
import com.project.oag.entity.User;
import com.project.oag.exceptions.InvalidBidException;
import com.project.oag.repository.BidArtRepository;
import com.project.oag.repository.BidRepository;

@Service
public class BidService {

    
    @Autowired
    private BidArtRepository bidArtRepository;
	
    @Autowired
    private final BidRepository bidRepository;
    
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }
    public void saveBid(Bid bid) {
        bidRepository.save(bid);
    }

    public void deleteBidById(Long id) {
        bidRepository.deleteById(id);
    }

    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }

    public Bid getBid(Long id) {
        return bidRepository.findById(id).orElse(null);
    }


    public Bid updateBid(Bid bid, Long id) {
        bid.setId(id);
        return bidRepository.save(bid);
    }

    public void deleteBid(Long id) {
        bidRepository.deleteById(id);
    }


    public Bid placeBid(Bid bid) throws InvalidBidException {
        BidArt bidArt = bid.getArtwork();
        BigDecimal currentHighestBidAmount = getCurrentHighestBidAmount(bidArt);
        if (bid.getAmount().compareTo(currentHighestBidAmount) <= 0) {
            throw new InvalidBidException("Bid amount must be greater than current highest bid amount.");
        }
        bid.setTimestamp(LocalDateTime.now());
        bid = bidRepository.save(bid);
        bidArt.getBids().add(bid);
        return bid;
    }

    public BigDecimal getCurrentHighestBidAmount(BidArt bidArt) {
        List<Bid> bids = bidArt.getBids();
        if (bids.isEmpty()) {
            return BigDecimal.valueOf(bidArt.getInitialAmount());
        } else {
            Bid highestBid = Collections.max(bids, Comparator.comparing(Bid::getAmount));
            return highestBid.getAmount();
        }
    }

    /*@Scheduled(fixedDelay = 10000)
    public void checkBidEndTimes() {
        LocalDateTime now = LocalDateTime.now();
        List<Bid> bids = bidRepository.findAll();
        for (Bid bid : bids) {
            if (bid.getBidEndTime().isBefore(now) && !bid.isBiddingClosed()) {
                bid.setBiddingClosed(true);
                // Determine the winner of the auction
                Bid winningBid = // get the winning bid
                // Notify the bidders of the result
                // Send a message to the WebSocket clients to update their UIs
            }
        }
    }*/

    
    @Scheduled(fixedDelay = 10000)
    public void checkBidEndTimes() {
        LocalDateTime now = LocalDateTime.now();
        List<Bid> bids = bidRepository.findAll();
        for (Bid bid : bids) {
            if (bid.getBidEndTime().isBefore(now) && !bid.isBiddingClosed()) {
                bid.setBiddingClosed(true);
                Bid winningBid = determineWinningBid(bid.getArtwork());
                notifyBidders(bid.getArtwork(), winningBid);
                messagingTemplate.convertAndSend("/topic/bids/" + bid.getArtwork().getId(), "update");
                bidRepository.save(bid);
            }
        }
    }

    private Bid determineWinningBid(BidArt artwork) {
        List<Bid> bids = bidRepository.findByArtworkOrderByAmountDescTimestampAsc(artwork);
        if (!bids.isEmpty()) {
            return bids.get(0);
        }
        return null;
    }

    private void notifyBidders(BidArt artwork, Bid winningBid) {
        List<Bid> bids = bidRepository.findByArtworkOrderByAmountDescTimestampAsc(artwork);
        for (Bid bid : bids) {
            User bidder = bid.getUser();
            String message = "";
            if (bid.equals(winningBid)) {
                message = "Congratulations! You won the auction for " + artwork.getTitle() + " with a bid of " + bid.getAmount() + ".";
            } else {
                message = "Unfortunately, you did not win the auction for " + artwork.getTitle() + ". The winning bid was " + winningBid.getAmount() + ".";
            }
            // Send a message to the bidder's email or mobile app using a notification service
        }
    }

    @Scheduled(fixedDelay = 60000) // runs every minute
    public void startBidding() {
        LocalDateTime now = LocalDateTime.now();
        List<BidArt> bidArtsToStart = bidArtRepository.findByStartingTimeLessThanEqualAndBiddingStartedFalse(now);
        for (BidArt bidArt : bidArtsToStart) {
            bidArt.setBiddingStarted(true);
            bidArtRepository.save(bidArt);
        }
    }


}

