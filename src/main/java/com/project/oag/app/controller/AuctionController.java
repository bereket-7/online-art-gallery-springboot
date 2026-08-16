package com.project.oag.app.controller;

import com.project.oag.app.dto.AuctionRequestDto;
import com.project.oag.app.dto.BidRequestDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.AuctionService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/auctions")
@Tag(name = "Auctions")
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

    @GetMapping("/{id}/bids")
    public ResponseEntity<GenericResponse> getBids(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Bids retrieved", auctionService.getBids(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getAuction(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Auction retrieved", auctionService.getAuction(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponse> createAuction(HttpServletRequest request,
                                                         @Valid @RequestBody AuctionRequestDto dto) {
        boolean admin = hasAuthority("ADMIN_MODIFY_ARTWORK");
        return prepareResponse(HttpStatus.CREATED, "Auction created",
                auctionService.createAuction(dto, resolveUserId(request), admin));
    }

    @PostMapping("/{id}/bid")
    @PreAuthorize("hasAuthority('USER_PLACE_BID')")
    public ResponseEntity<GenericResponse> placeBid(@PathVariable Long id,
                                                     HttpServletRequest request,
                                                     @Valid @RequestBody BidRequestDto dto) {
        return prepareResponse(HttpStatus.CREATED, "Bid placed",
                auctionService.placeBid(id, resolveUserId(request), dto.getAmount()));
    }

    @PostMapping("/{id}/watch")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponse> watch(@PathVariable Long id, HttpServletRequest request) {
        auctionService.watch(id, resolveUserId(request));
        return prepareResponse(HttpStatus.OK, "Watching auction", null);
    }

    @DeleteMapping("/{id}/watch")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponse> unwatch(@PathVariable Long id, HttpServletRequest request) {
        auctionService.unwatch(id, resolveUserId(request));
        return prepareResponse(HttpStatus.OK, "Stopped watching auction", null);
    }

    private boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> authority.equals(a.getAuthority()));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
