package com.project.oag.bidding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class BidArtService {
    @Autowired
    private BidArtRepository bidArtRepository;
    public Optional<BidArt> getBidArtById(Long id) {
        return bidArtRepository.findById(id);
    }
    public void saveBidArt(BidArt bidArt) {
        bidArtRepository.save(bidArt);
    }
    public List<BidArt> getAllBidArts() {
        return bidArtRepository.findAll();
    }
}