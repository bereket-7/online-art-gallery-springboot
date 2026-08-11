package com.project.oag.app.service;

import com.project.oag.app.dto.PaymentStatus;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.PaymentLog;
import com.project.oag.app.entity.User;
import com.project.oag.config.properties.ChapaConfig;
import com.project.oag.exceptions.GeneralException;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ChapaService {

    private final ChapaConfig chapaConfig;
    private final PaymentLogService paymentLogService;

    public ChapaService(ChapaConfig chapaConfig, PaymentLogService paymentLogService) {
        this.chapaConfig = chapaConfig;
        this.paymentLogService = paymentLogService;
    }

    @Transactional
    public PaymentResponse initiatePayment(Order order) {
        try {
            BigDecimal totalPrice = order.getTotalAmount();
            User user = order.getUser();

            Customization customization = new Customization()
                    .setDescription("Payment for Order " + order.getId())
                    .setTitle("Kelem OAG Checkout");
            String txRef = Util.generateToken();
            PostData postData = new PostData()
                    .setAmount(totalPrice)
                    .setCurrency("ETB")
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setEmail(user.getEmail())
                    .setReturnUrl(chapaConfig.getReturnUrlBase() + "/api/v1/chapa/callback?tx_ref=" + txRef)
                    .setTxRef(txRef)
                    .setCustomization(customization);

            Chapa chapa = new Chapa(chapaConfig.getSecretKey());
            InitializeResponseData response = chapa.initialize(postData);
            String checkOutUrl = response.getData().getCheckOutUrl();

            PaymentLog paymentLog = new PaymentLog();
            paymentLog.setAmount(totalPrice);
            paymentLog.setPaymentStatus(PaymentStatus.INITIALIZED);
            paymentLog.setToken(txRef);
            paymentLog.setUser(user);
            PaymentLog saved = paymentLogService.createPaymentLog(paymentLog);

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setCheckOutUrl(checkOutUrl);
            paymentResponse.setTxRef(txRef);
            paymentResponse.setPaymentLog(saved);
            return paymentResponse;
        } catch (Throwable e) {
            throw new GeneralException("Failed to create payment: " + e.getMessage());
        }
    }

    @Transactional
    public PaymentLog verify(String txRef) {
        try {
            Chapa chapa = new Chapa(chapaConfig.getSecretKey());
            VerifyResponseData verify = chapa.verify(txRef);

            PaymentLog paymentLog = paymentLogService.findByToken(txRef);
            if (verify.getStatusCode() == 200) {
                paymentLog.setPaymentStatus(PaymentStatus.VERIFIED);
            } else {
                paymentLog.setPaymentStatus(PaymentStatus.FAILED);
            }
            return paymentLogService.updatePaymentLog(paymentLog);
        } catch (Throwable e) {
            throw new GeneralException("failed to verify payment");
        }
    }
}
