package com.project.oag.app.controller;

import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.dto.PayoutAmountRequestDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.PayoutService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/payouts")
@Tag(name = "Payouts")
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
        return prepareResponse(HttpStatus.OK, "Wallet retrieved",
                CommerceMappers.toWalletDto(payoutService.getWallet(resolveUserId(request))));
    }

    @PostMapping("/request")
    @PreAuthorize("hasAuthority('ARTIST_REQUEST_PAYOUT')")
    public ResponseEntity<GenericResponse> requestPayout(HttpServletRequest request,
                                                         @Valid @RequestBody PayoutAmountRequestDto dto) {
        return prepareResponse(HttpStatus.CREATED, "Payout requested",
                CommerceMappers.toPayoutDto(payoutService.requestPayout(resolveUserId(request), dto.getAmount())));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ARTIST_REQUEST_PAYOUT')")
    public ResponseEntity<GenericResponse> getMyPayouts(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Payouts retrieved",
                payoutService.getArtistPayouts(resolveUserId(request)).stream()
                        .map(CommerceMappers::toPayoutDto)
                        .toList());
    }

    @GetMapping("/admin/pending")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_PAYOUTS')")
    public ResponseEntity<GenericResponse> getPendingPayouts() {
        return prepareResponse(HttpStatus.OK, "Pending payouts",
                payoutService.getPendingPayouts().stream().map(CommerceMappers::toPayoutDto).toList());
    }

    @PatchMapping("/admin/{id}/approve")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_PAYOUTS')")
    public ResponseEntity<GenericResponse> approvePayout(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Payout approved",
                CommerceMappers.toPayoutDto(payoutService.approvePayout(id)));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
