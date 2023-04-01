package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Bidder;
import com.project.oag.repository.BidderRepository;

@Service
public class BidderService {

	@Autowired
	private final BidderRepository bidderRepository;
	
    @Autowired
    public BidderService(BidderRepository bidderRepository) {
        this.bidderRepository = bidderRepository;
    }

    public List<Bidder> getAllBidders() {
        return bidderRepository.findAll();
    }

    public Optional<Bidder> getBidderById(Long id) {
        return bidderRepository.findById(id);
    }

    public void saveBidder(Bidder bidder) {
        bidderRepository.save(bidder);
    }

    public void deleteBidderById(Long id) {
        bidderRepository.deleteById(id);
    }
}
