package com.project.oag.bidding;

import com.project.oag.entity.User;
import com.project.oag.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
@Service
public class BidService {
    @Autowired
    private BidArtRepository bidArtRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BidArtService bidArtService;
    public void placeBid(Long artworkId, Long userId, BigDecimal amount) {
        BidArt artwork = bidArtRepository.findById(artworkId)
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getSelectedForBid()) {
            throw new IllegalStateException("User is not selected for bidding");
        }
        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isAfter(artwork.getBidEndTime())) {
            throw new IllegalStateException("Bidding has ended");
        }
        BigDecimal currentPrice = artwork.getInitialAmount();
        List<Bid> bids = artwork.getBids();

        if (!bids.isEmpty()) {
            BigDecimal highestBidAmount = bids.stream()
                    .map(Bid::getAmount)
                    .max(BigDecimal::compareTo)
                    .orElse(currentPrice);

            currentPrice = highestBidAmount;
        }
        if (amount.compareTo(currentPrice) <= 0) {
            throw new IllegalArgumentException("Bid amount must be greater than the current price");
        }
        Bid bid = new Bid();
        bid.setArtwork(artwork);
        bid.setUser(user);
        bid.setAmount(amount);
        bid.setTimestamp(currentTime);
        bidRepository.save(bid);
        scheduleBidClosing();
    }

    @Scheduled(fixedDelay = 60000)
    private void scheduleBidClosing() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<BidArt> artworks = bidArtService.getAllBidArts();

        for (BidArt artwork : artworks) {
            if (currentTime.isAfter(artwork.getBidEndTime()) && !artwork.isBiddingClosed()) {
                closeBidding(artwork);
                announceWinner(artwork);
            }
        }
    }

    private void closeBidding(BidArt artwork) {
        artwork.setBiddingClosed(true);
        bidArtRepository.save(artwork);
    }
    private User announceWinner(BidArt artwork) {
        List<Bid> bids = artwork.getBids();

        if (bids.isEmpty()) {
            return null;
        }
        Bid winningBid = bids.stream()
                .max(Comparator.comparing(Bid::getAmount))
                .orElseThrow(() -> new IllegalStateException("No winning bid found"));
        User winner = winningBid.getUser();
        return winner;
    }


}