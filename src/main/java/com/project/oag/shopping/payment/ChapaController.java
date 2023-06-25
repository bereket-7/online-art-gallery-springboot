package com.project.oag.shopping.payment;

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

@RestController
@RequestMapping("/payment")
public class ChapaController {
    @Autowired
    PaymentLogService paymentLogService;
    @PostMapping("/initialize")
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
                .setReturnUrl("http://localhost:8082/payment/verify/" + txRef) // send verification url: /verify/{txRef}
                .setTxRef(txRef)
                .setCustomization(customization);
        Chapa chapa = new Chapa("CHASECK_TEST-fJ1YgTYDTBppmzQ6kGdIZ6GFZQLXilZ0");
        InitializeResponseData response = chapa.initialize(postData);
        String checkOutUrl = response.getData().getCheckOutUrl();
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setCheckOutUrl(checkOutUrl);
        paymentResponse.setTxRef(txRef);

        PaymentLog paymentLog = new PaymentLog();
        paymentLog.setAmount(postData.getAmount());
        paymentLog.setEmail(loggedInUser.getEmail());
        paymentLog.setStatus(Status.INTIALIZED);
        paymentLog.setToken(txRef);
        paymentLogService.createPaymentLog(paymentLog);


        //paymentResponse.getCheckOutUrl();
        //paymentResponse.getTxRef();
        return ResponseEntity.ok(paymentResponse);
    }
    @GetMapping("/verify/{txRef}")
    public ResponseEntity<Void> verify(@PathVariable("txRef") String txRef) throws Throwable {
        Chapa chapa = new Chapa("CHASECK_TEST-fJ1YgTYDTBppmzQ6kGdIZ6GFZQLXilZ0");
        VerifyResponseData verify = chapa.verify(txRef);
        if(verify.getStatusCode() == 200) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
