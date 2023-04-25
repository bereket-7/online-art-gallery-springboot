package com.project.oag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Bid;
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
}
