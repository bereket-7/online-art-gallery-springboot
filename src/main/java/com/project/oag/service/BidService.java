package com.project.oag.service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Bid> getBidById(Long id) {
        return bidRepository.findById(id);
    }

    public void saveBid(Bid bid) {
        bidRepository.save(bid);
    }

    public void deleteBidById(Long id) {
        bidRepository.deleteById(id);
    }

}
