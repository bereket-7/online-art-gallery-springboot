package com.project.oag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.BidArt;
import com.project.oag.repository.BidArtRepository;

@Service
public class BidArtService {
	
	   @Autowired
	    private BidArtRepository bidArtRepository;

	    public BidArt createBidArt(BidArt bidArt) {
	        return bidArtRepository.save(bidArt);
	    }

	    public BidArt getBidArt(Long id) {
	        return bidArtRepository.findById(id).orElse(null);
	    }

	    public List<BidArt> getAllBidArts() {
	        return bidArtRepository.findAll();
	    }

	    public BidArt updateBidArt(BidArt bidArt, Long id) {
	        bidArt.setId(id);
	        return bidArtRepository.save(bidArt);
	    }

	    public void deleteBidArt(Long id) {
	        bidArtRepository.deleteById(id);
	    }
}
