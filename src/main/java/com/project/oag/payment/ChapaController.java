package com.project.oag.payment;

import com.project.oag.security.service.CustomUserDetailsService;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;

public class ChapaController {

    @Autowired
    CustomUserDetailsService userService;

    @PostMapping
    public void pay(Principal principal) throws Throwable {
        principal.getName();
        Customization customization = new Customization()
                .setDescription("Test")
                .setTitle("Test");

        String txRef = Util.generateToken();
        PostData postData = new PostData()
                .setAmount(new BigDecimal("100"))
                .setCurrency("ETB")
                .setFirstName("Abebe")
                .setLastName("Bikila")
                .setEmail("abebe@bikila.com")
                .setTxRef(txRef)
                .setCustomization(customization);

        Chapa chapa = new Chapa("");
        InitializeResponseData response = chapa.initialize(postData);
        String checkOutUrl = response.getData().getCheckOutUrl();

        VerifyResponseData verify = chapa.verify(txRef);
    }
}
