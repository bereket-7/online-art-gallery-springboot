package com.project.oag.app.controller;

import com.project.oag.app.entity.Auction;
import com.project.oag.app.entity.Bid;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.AuctionService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final UserRepository userRepository;

    public AuctionController(AuctionService auctionService, UserRepository userRepository) {
        this.auctionService = auctionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getActiveAuctions() {
        return prepareResponse(HttpStatus.OK, "Active auctions", auctionService.getActiveAuctions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getAuction(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Auction retrieved", auctionService.getAuction(id));
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<GenericResponse> getBids(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Bids retrieved", auctionService.getBids(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> createAuction(@RequestParam Long artworkId,
                                                           @RequestParam Timestamp startTime,
                                                           @RequestParam Timestamp endTime,
                                                           @RequestParam(required = false) BigDecimal reservePrice) {
        Auction auction = auctionService.createAuction(artworkId, startTime, endTime, reservePrice);
        return prepareResponse(HttpStatus.CREATED, "Auction created", auction);
    }

    @PostMapping("/{id}/bid")
    @PreAuthorize("hasAuthority('USER_PLACE_BID')")
    public ResponseEntity<GenericResponse> placeBid(@PathVariable Long id,
                                                     HttpServletRequest request,
                                                     @RequestParam BigDecimal amount) {
        Bid bid = auctionService.placeBid(id, resolveUserId(request), amount);
        return prepareResponse(HttpStatus.CREATED, "Bid placed", bid);
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
