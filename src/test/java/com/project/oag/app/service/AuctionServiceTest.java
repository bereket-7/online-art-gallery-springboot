package com.project.oag.app.service;

import com.project.oag.app.dto.AuctionStatus;
import com.project.oag.app.dto.BidResponseDto;
import com.project.oag.app.entity.Auction;
import com.project.oag.app.entity.Bid;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.AuctionRepository;
import com.project.oag.app.repository.AuctionWatchRepository;
import com.project.oag.app.repository.BidRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private BidRepository bidRepository;
    @Mock
    private ArtworkRepository artworkRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuctionWatchRepository auctionWatchRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private NotificationWebSocketService notificationService;

    @InjectMocks
    private AuctionService auctionService;

    private Auction auction;
    private User bidder;

    @BeforeEach
    void setUp() {
        auction = new Auction();
        auction.setId(1L);
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setStartTime(Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));
        auction.setEndTime(Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS)));
        auction.setCurrentBid(new BigDecimal("100"));
        bidder = new User();
        bidder.setId(8L);
    }

    @Test
    void placeBid_RejectsWhenNotHigherThanCurrent() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        assertThrows(ConflictException.class, () -> auctionService.placeBid(1L, 8L, new BigDecimal("100")));
        verify(bidRepository, never()).save(any());
    }

    @Test
    void placeBid_SucceedsWhenHigher() {
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(userRepository.findById(8L)).thenReturn(Optional.of(bidder));
        Bid saved = new Bid();
        saved.setId(3L);
        saved.setAuction(auction);
        saved.setBidder(bidder);
        saved.setAmount(new BigDecimal("150"));
        when(bidRepository.save(any(Bid.class))).thenReturn(saved);
        when(auctionRepository.save(auction)).thenReturn(auction);

        BidResponseDto dto = auctionService.placeBid(1L, 8L, new BigDecimal("150"));

        assertEquals(new BigDecimal("150"), dto.getAmount());
        assertEquals(new BigDecimal("150"), auction.getCurrentBid());
    }
}
