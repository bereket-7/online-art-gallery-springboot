package com.project.oag.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Bid;
import com.project.oag.entity.BidArt;
import com.project.oag.repository.BidRepository;

@Service
public class BidService {
	
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

 
    @Scheduled(fixedDelay = 10000)
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
    }

    public BigDecimal getCurrentHighestBidAmount(BidArt bidArt) {
        List<Bid> bids = bidArt.getBids();
        if (bids.isEmpty()) {
            return bidArt.getInitialAmount();
        } else {
            Bid highestBid = Collections.max(bids, Comparator.comparing(Bid::getAmount));
            return highestBid.getAmount();
        }
    }
}

