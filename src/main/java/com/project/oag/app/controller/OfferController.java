package com.project.oag.app.controller;

import com.project.oag.app.dto.OfferRequestDto;
import com.project.oag.app.dto.OfferStatusRequestDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.OfferService;
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
@RequestMapping("api/v1/offers")
@Tag(name = "Offers")
public class OfferController {

    private final OfferService offerService;
    private final UserRepository userRepository;

    public OfferController(OfferService offerService, UserRepository userRepository) {
        this.offerService = offerService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_MAKE_OFFER')")
    public ResponseEntity<GenericResponse> makeOffer(HttpServletRequest request,
                                                     @Valid @RequestBody OfferRequestDto dto) {
        return prepareResponse(HttpStatus.CREATED, "Offer submitted",
                offerService.makeOffer(dto.getArtworkId(), resolveUserId(request), dto.getAmount()));
    }

    @GetMapping("/artwork/{artworkId}")
    @PreAuthorize("hasAnyAuthority('ARTIST_VIEW_OWN_ARTWORK', 'ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getOffersForArtwork(@PathVariable Long artworkId) {
        return prepareResponse(HttpStatus.OK, "Offers retrieved", offerService.getOffersForArtwork(artworkId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER_MAKE_OFFER')")
    public ResponseEntity<GenericResponse> getMyOffers(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Offers retrieved",
                offerService.getOffersByBuyer(resolveUserId(request)));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ARTIST_ACCEPT_OFFER')")
    public ResponseEntity<GenericResponse> getPending(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Pending offers",
                offerService.getPendingForArtist(resolveUserId(request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ARTIST_ACCEPT_OFFER', 'ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> updateOfferStatus(@PathVariable Long id,
                                                             HttpServletRequest request,
                                                             @Valid @RequestBody OfferStatusRequestDto dto) {
        boolean admin = hasAuthority("ADMIN_MODIFY_ARTWORK");
        return prepareResponse(HttpStatus.OK, "Offer updated",
                offerService.updateOfferStatus(id, dto.getStatus(), resolveUserId(request), admin));
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
