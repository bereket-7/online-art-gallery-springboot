package com.project.oag.shopping.payment;

import com.project.oag.user.User;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
@RestController
@RequestMapping("/payment")
public class ChapaController {
    @PostMapping
    public ResponseEntity<PaymentResponse> pay() throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        Customization customization = new Customization()
                .setDescription("Test")
                .setTitle("Test");
        String txRef = Util.generateToken();
        PostData postData = new PostData()
                .setAmount(new BigDecimal("100"))
                .setCurrency("ETB")
                .setFirstName(loggedInUser.getFirstname())
                .setLastName(loggedInUser.getLastname())
                .setEmail(loggedInUser.getEmail())
                .setTxRef(txRef)
                .setCustomization(customization);


        Chapa chapa = new Chapa("CHASECK_TEST-fJ1YgTYDTBppmzQ6kGdIZ6GFZQLXilZ0");
        InitializeResponseData response = chapa.initialize(postData);
        String checkOutUrl = response.getData().getCheckOutUrl();
        VerifyResponseData verify = chapa.verify(txRef);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCheckOutUrl(checkOutUrl);
        paymentResponse.setTxRef(txRef);
        //paymentResponse.getCheckOutUrl();
        //paymentResponse.getTxRef();
        return ResponseEntity.ok(paymentResponse);
    }
}
