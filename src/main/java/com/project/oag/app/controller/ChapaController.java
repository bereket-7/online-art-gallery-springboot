package com.project.oag.app.controller;

import com.project.oag.app.service.PaymentLogService;
import com.project.oag.app.service.PaymentResponse;
import com.project.oag.app.service.CartService;
import com.project.oag.app.model.PaymentLog;
import com.project.oag.app.dto.Status;
import com.project.oag.user.User;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/payment")
public class ChapaController {
    private final PaymentLogService paymentLogService;

    private final CartService cartService;


@Autowired
    public ChapaController(PaymentLogService paymentLogService, PaymentLog paymentLog, CartService cartService) {
        this.paymentLogService = paymentLogService;
        this.paymentLog = paymentLog;
        this.cartService = cartService;
    }
    private final PaymentLog paymentLog;

    @PostMapping("/initialize")
    public ResponseEntity<PaymentResponse> pay() throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        int totalPrice = cartService.calculateTotalPrice(loggedInUser.getUsername());

        Customization customization = new Customization()
                .setDescription("Payment for Kelem OAG")
                .setTitle("Kelem OAG");
        String txRef = Util.generateToken();
        PostData postData = new PostData()
                .setAmount(BigDecimal.valueOf(totalPrice))
                //.setAmount(new BigDecimal("10000"))
                .setCurrency("ETB")
                .setFirstName(loggedInUser.getFirstname())
                .setLastName(loggedInUser.getLastname())
                .setEmail(loggedInUser.getEmail())
                .setReturnUrl("http://localhost:8080/paymentSuccess/" + txRef) // send verification url: /verify/{txRef}
                .setTxRef(txRef)
                .setCustomization(customization);

        Chapa chapa = new Chapa("CHASECK_TEST-fJ1YgTYDTBppmzQ6kGdIZ6GFZQLXilZ0");
        InitializeResponseData response = chapa.initialize(postData);
        String checkOutUrl = response.getData().getCheckOutUrl();

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCheckOutUrl(checkOutUrl);
        paymentResponse.setTxRef(txRef);

        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setAmount(BigDecimal.valueOf(totalPrice));
        paymentLog.setEmail(loggedInUser.getEmail());
        paymentLog.setCreateDate(LocalDateTime.now());
        paymentLog.setStatus(Status.INTIALIZED);
        paymentLog.setToken(txRef);
        paymentLogService.createPaymentLog(paymentLog);

        return ResponseEntity.ok(paymentResponse);
    }
    @GetMapping("/verify/{txRef}")
    public ResponseEntity<Void> verify(@PathVariable("txRef") String txRef) throws Throwable {
        Chapa chapa = new Chapa("CHASECK_TEST-fJ1YgTYDTBppmzQ6kGdIZ6GFZQLXilZ0");
        VerifyResponseData verify = chapa.verify(txRef);
        if(verify.getStatusCode() == 200) {
            paymentLogService.findByToken(txRef);
            paymentLog.setStatus(Status.VERIFIED);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
