package com.project.oag.app.controller;

import com.project.oag.app.dto.OfferStatus;
import com.project.oag.app.entity.Offer;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.OfferService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/offers")
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
                                                     @RequestParam Long artworkId,
                                                     @RequestParam BigDecimal amount) {
        Offer offer = offerService.makeOffer(artworkId, resolveUserId(request), amount);
        return prepareResponse(HttpStatus.CREATED, "Offer submitted", offer);
    }

    @GetMapping("/artwork/{artworkId}")
    @PreAuthorize("hasAnyAuthority('ARTIST_VIEW_OWN_ARTWORK', 'ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getOffersForArtwork(@PathVariable Long artworkId) {
        return prepareResponse(HttpStatus.OK, "Offers retrieved", offerService.getOffersForArtwork(artworkId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER_MAKE_OFFER')")
    public ResponseEntity<GenericResponse> getMyOffers(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Offers retrieved", offerService.getOffersByBuyer(resolveUserId(request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> updateOfferStatus(@PathVariable Long id,
                                                             @RequestParam OfferStatus status) {
        return prepareResponse(HttpStatus.OK, "Offer updated", offerService.updateOfferStatus(id, status));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
