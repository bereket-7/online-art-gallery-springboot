package com.project.oag.app.service;

import com.project.oag.app.dto.AuctionRequestDto;
import com.project.oag.app.dto.AuctionResponseDto;
import com.project.oag.app.dto.AuctionStatus;
import com.project.oag.app.dto.BidResponseDto;
import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Auction;
import com.project.oag.app.entity.AuctionWatch;
import com.project.oag.app.entity.Bid;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.AuctionRepository;
import com.project.oag.app.repository.AuctionWatchRepository;
import com.project.oag.app.repository.BidRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ConflictException;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuctionService {

    private static final int ANTI_SNIPE_MINUTES = 5;

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final AuctionWatchRepository auctionWatchRepository;
    private final OrderService orderService;
    private final NotificationWebSocketService notificationService;

    public AuctionService(AuctionRepository auctionRepository,
                          BidRepository bidRepository,
                          ArtworkRepository artworkRepository,
                          UserRepository userRepository,
                          AuctionWatchRepository auctionWatchRepository,
                          OrderService orderService,
                          NotificationWebSocketService notificationService) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.auctionWatchRepository = auctionWatchRepository;
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @Transactional
    public AuctionResponseDto createAuction(AuctionRequestDto request, Long actorId, boolean admin) {
        Artwork artwork = artworkRepository.findById(request.getArtworkId())
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        if (!admin && (artwork.getUser() == null || !artwork.getUser().getId().equals(actorId))) {
            throw new UserAuthorizationException("Only the owning artist or an admin can create an auction");
        }
        Auction auction = new Auction();
        auction.setArtwork(artwork);
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        auction.setReservePrice(request.getReservePrice());
        auction.setStatus(AuctionStatus.ACTIVE);
        return CommerceMappers.toAuctionDto(auctionRepository.save(auction));
    }

    @Transactional
    public BidResponseDto placeBid(Long auctionId, Long bidderId, BigDecimal amount) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new ConflictException("Auction is not active");
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.before(auction.getStartTime()) || now.after(auction.getEndTime())) {
            throw new ConflictException("Auction is not open for bidding");
        }
        BigDecimal minimum = auction.getCurrentBid() != null
                ? auction.getCurrentBid()
                : (auction.getReservePrice() != null ? auction.getReservePrice() : BigDecimal.ZERO);
        boolean mustExceed = auction.getCurrentBid() != null;
        if (mustExceed && amount.compareTo(minimum) <= 0) {
            throw new ConflictException("Bid must exceed current bid");
        }
        if (!mustExceed && amount.compareTo(minimum) < 0) {
            throw new ConflictException("Bid must meet reserve price");
        }

        User bidder = userRepository.findById(bidderId)
                .orElseThrow(() -> new ResourceNotFoundException("Bidder not found"));

        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(bidder);
        bid.setAmount(amount);
        Bid saved = bidRepository.save(bid);

        auction.setCurrentBid(amount);
        long millisLeft = auction.getEndTime().getTime() - now.getTime();
        if (millisLeft <= ANTI_SNIPE_MINUTES * 60_000L) {
            auction.setEndTime(Timestamp.from(now.toInstant().plus(ANTI_SNIPE_MINUTES, ChronoUnit.MINUTES)));
        }
        auctionRepository.save(auction);
        return CommerceMappers.toBidDto(saved);
    }

    public AuctionResponseDto getAuction(Long id) {
        return CommerceMappers.toAuctionDto(auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found")));
    }

    public List<AuctionResponseDto> getActiveAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE).stream()
                .map(CommerceMappers::toAuctionDto)
                .toList();
    }

    public List<BidResponseDto> getBids(Long auctionId) {
        return bidRepository.findByAuctionIdOrderByAmountDesc(auctionId).stream()
                .map(CommerceMappers::toBidDto)
                .toList();
    }

    @Transactional
    public void watch(Long auctionId, Long userId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
        if (auctionWatchRepository.existsByAuctionIdAndUserId(auctionId, userId)) {
            return;
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        AuctionWatch watch = new AuctionWatch();
        watch.setAuction(auction);
        watch.setUser(user);
        auctionWatchRepository.save(watch);
    }

    @Transactional
    public void unwatch(Long auctionId, Long userId) {
        auctionWatchRepository.deleteByAuctionIdAndUserId(auctionId, userId);
    }

    @Transactional
    public int closeExpiredAuctions() {
        List<Auction> expired = auctionRepository.findByStatusAndEndTimeBefore(
                AuctionStatus.ACTIVE, Timestamp.from(Instant.now()));
        for (Auction auction : expired) {
            auction.setStatus(AuctionStatus.ENDED);
            boolean reserveMet = auction.getReservePrice() == null
                    || (auction.getCurrentBid() != null && auction.getCurrentBid().compareTo(auction.getReservePrice()) >= 0);
            if (reserveMet && auction.getCurrentBid() != null) {
                Bid winning = bidRepository.findByAuctionIdOrderByAmountDesc(auction.getId()).stream()
                        .findFirst()
                        .orElse(null);
                if (winning != null) {
                    auction.setWinner(winning.getBidder());
                    OrderService.ArtworkLine line = new OrderService.ArtworkLine(
                            auction.getArtwork(),
                            auction.getArtwork().getUser(),
                            1,
                            winning.getAmount(),
                            winning.getAmount());
                    var order = orderService.createPendingOrder(winning.getBidder(), line,
                            winning.getBidder().getFirstName(), winning.getBidder().getLastName());
                    notificationService.sendUserNotification(winning.getBidder().getEmail(),
                            "You won auction #" + auction.getId() + ". Pending order #" + order.getId() + " is ready for checkout.",
                            "AUCTION");
                }
            }
            auctionRepository.save(auction);
        }
        return expired.size();
    }
}
