package com.project.oag.payment;

import com.project.oag.security.service.CustomUserDetailsService;
import com.project.oag.user.User;
import com.project.oag.user.UserRepository;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;

public class ChapaController {
    @Autowired
    private CustomUserDetailsService userService;
    @PostMapping
    public void pay() throws Throwable {
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
        Chapa chapa = new Chapa("");
        InitializeResponseData response = chapa.initialize(postData);
        String checkOutUrl = response.getData().getCheckOutUrl();

        VerifyResponseData verify = chapa.verify(txRef);
    }
}
