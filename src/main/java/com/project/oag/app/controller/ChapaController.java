package com.project.oag.app.controller;

import com.project.oag.app.dto.PaymentStatus;
import com.project.oag.app.model.PaymentLog;
import com.project.oag.app.model.User;
import com.project.oag.app.service.CartService;
import com.project.oag.app.service.ChapaService;
import com.project.oag.app.service.PaymentLogService;
import com.project.oag.app.service.PaymentResponse;
import com.project.oag.common.GenericResponse;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/payment")
public class ChapaController {
    private final PaymentLogService paymentLogService;
    private final PaymentLog paymentLog;
    private final CartService cartService;
    private final ChapaService chapaService;

    public ChapaController(PaymentLogService paymentLogService, PaymentLog paymentLog, CartService cartService, ChapaService chapaService) {
        this.paymentLogService = paymentLogService;
        this.paymentLog = paymentLog;
        this.cartService = cartService;
        this.chapaService = chapaService;
    }

    @PostMapping("/initialize")
    public ResponseEntity<GenericResponse> pay(HttpServletRequest request) throws Throwable {
        return chapaService.pay(request);
    }
    @GetMapping("/verify/{txRef}")
    public ResponseEntity<GenericResponse> verify(@PathVariable("txRef") String txRef) throws Throwable {
        return chapaService.verify(txRef);
    }
}
