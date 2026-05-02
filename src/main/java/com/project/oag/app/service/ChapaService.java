package com.project.oag.app.service;

import com.project.oag.app.dto.PaymentStatus;
import com.project.oag.app.entity.PaymentLog;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;
@Service
public class ChapaService {

    @Value("${secretKey}")
    private static String secretKey;
    private final CartService cartService;
    private final PaymentLogService paymentLogService;
    private final UserRepository userRepository;

    public ChapaService(CartService cartService, PaymentLogService paymentLogService, UserRepository userRepository) {
        this.cartService = cartService;
        this.paymentLogService = paymentLogService;
        this.userRepository = userRepository;
    }

    public PaymentResponse initiatePayment(Order order) throws Throwable {
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
                    .setReturnUrl("http://localhost:8080/paymentSuccess/" + txRef)
                    .setTxRef(txRef)
                    .setCustomization(customization);

            Chapa chapa = new Chapa(secretKey);
            InitializeResponseData response = chapa.initialize(postData);
            String checkOutUrl = response.getData().getCheckOutUrl();

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setCheckOutUrl(checkOutUrl);
            paymentResponse.setTxRef(txRef);

            PaymentLog paymentLog = new PaymentLog();
            paymentLog.setAmount(totalPrice);
            paymentLog.setPaymentStatus(PaymentStatus.INTIALIZED);
            paymentLog.setToken(txRef);
            paymentLogService.createPaymentLog(paymentLog);

            // Optional: link payment log to order here if needed immediately, or let orchestrator do it
            
            return paymentResponse;
        } catch (Throwable e) {
            throw new GeneralException("Failed to create payment: " + e.getMessage());
        }
    }

    public PaymentLog verify(String txRef) throws Throwable {
        try {
            Chapa chapa = new Chapa(secretKey);
            VerifyResponseData verify = chapa.verify(txRef);
            
            PaymentLog paymentLog = paymentLogService.findByToken(txRef);
            if (verify.getStatusCode() == 200) {
                paymentLog.setPaymentStatus(PaymentStatus.VERIFIED);
                // Assume save is cascaded or explicitly save
            } else {
                paymentLog.setPaymentStatus(PaymentStatus.FAILED);
            }
            return paymentLog;

        } catch (Throwable e) {
            throw new GeneralException("failed to verify payment");
        }
    }

    private Long getUserId(HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }
}
