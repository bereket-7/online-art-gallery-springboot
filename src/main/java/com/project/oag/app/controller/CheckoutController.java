package com.project.oag.app.controller;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.service.PaymentResponse;
import com.project.oag.app.service.CheckoutService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_ADD_ORDER')")
    public ResponseEntity<GenericResponse> checkout(HttpServletRequest request, @RequestBody @Valid OrderRequestDto dto) throws Throwable {
        PaymentResponse response = checkoutService.checkout(request, dto);
        return prepareResponse(HttpStatus.OK, "Checkout Initialized. Proceed to payment Url.", response);
    }
}
