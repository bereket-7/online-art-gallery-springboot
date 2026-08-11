package com.project.oag.app.controller;

import com.project.oag.app.entity.ArtistWallet;
import com.project.oag.app.entity.PayoutRequest;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.PayoutService;
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
@RequestMapping("api/v1/payouts")
public class PayoutController {

    private final PayoutService payoutService;
    private final UserRepository userRepository;

    public PayoutController(PayoutService payoutService, UserRepository userRepository) {
        this.payoutService = payoutService;
        this.userRepository = userRepository;
    }

    @GetMapping("/wallet")
    @PreAuthorize("hasAuthority('ARTIST_REQUEST_PAYOUT')")
    public ResponseEntity<GenericResponse> getWallet(HttpServletRequest request) {
        ArtistWallet wallet = payoutService.getWallet(resolveUserId(request));
        return prepareResponse(HttpStatus.OK, "Wallet retrieved", wallet);
    }

    @PostMapping("/request")
    @PreAuthorize("hasAuthority('ARTIST_REQUEST_PAYOUT')")
    public ResponseEntity<GenericResponse> requestPayout(HttpServletRequest request,
                                                         @RequestParam BigDecimal amount) {
        PayoutRequest payout = payoutService.requestPayout(resolveUserId(request), amount);
        return prepareResponse(HttpStatus.CREATED, "Payout requested", payout);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ARTIST_REQUEST_PAYOUT')")
    public ResponseEntity<GenericResponse> getMyPayouts(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Payouts retrieved", payoutService.getArtistPayouts(resolveUserId(request)));
    }

    @GetMapping("/admin/pending")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_PAYOUTS')")
    public ResponseEntity<GenericResponse> getPendingPayouts() {
        return prepareResponse(HttpStatus.OK, "Pending payouts", payoutService.getPendingPayouts());
    }

    @PatchMapping("/admin/{id}/approve")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_PAYOUTS')")
    public ResponseEntity<GenericResponse> approvePayout(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Payout approved", payoutService.approvePayout(id));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
