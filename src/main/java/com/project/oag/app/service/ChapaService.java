package com.project.oag.app.service;

import com.project.oag.app.dto.PaymentStatus;
import com.project.oag.app.model.PaymentLog;
import com.project.oag.app.model.User;
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

import java.math.BigDecimal;
import java.util.Optional;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

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

    public ResponseEntity<GenericResponse> pay(HttpServletRequest request) throws Throwable {
        try {
            BigDecimal totalPrice = cartService.calculateTotalPrice(request);
            Long userId = getUserId(request);
            Optional<User> users = Optional.ofNullable(userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found.")));

            Customization customization = new Customization()
                    .setDescription("Payment for Kelem OAG")
                    .setTitle("Kelem OAG");
            String txRef = Util.generateToken();
            PostData postData = new PostData()
                    .setAmount(totalPrice)
                    .setCurrency("ETB")
                    .setFirstName(users.get().getFirstname())
                    .setLastName(users.get().getLastname())
                    .setEmail(users.get().getEmail())
                    .setReturnUrl("http://localhost:8080/paymentSuccess/" + txRef)
                    .setTxRef(txRef)
                    .setCustomization(customization);

            Chapa chapa = new Chapa(secretKey);
            InitializeResponseData response = chapa.initialize(postData);
            String checkOutUrl = response.getData().getCheckOutUrl();

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse = new PaymentResponse();
            paymentResponse.setCheckOutUrl(checkOutUrl);
            paymentResponse.setTxRef(txRef);

            PaymentLog paymentLog = new PaymentLog();
            paymentLog.setAmount(totalPrice);
            paymentLog.setUserId(userId);
            paymentLog.setPaymentStatus(PaymentStatus.INTIALIZED);
            paymentLog.setToken(txRef);
            paymentLogService.createPaymentLog(paymentLog);
            return prepareResponse(HttpStatus.OK,"PaymentLog",paymentLog);
        } catch (Throwable e) {
            throw new GeneralException("Failed to create payment");
        }
    }
    public ResponseEntity<GenericResponse> verify(String txRef) throws Throwable {
        try {
            Chapa chapa = new Chapa(secretKey);
            VerifyResponseData verify = chapa.verify(txRef);
            if (verify.getStatusCode() == 200) {
                paymentLogService.findByToken(txRef);
            }
                PaymentLog paymentLog = new PaymentLog();
                paymentLog.setPaymentStatus(PaymentStatus.VERIFIED);
                return prepareResponse(HttpStatus.OK, "", paymentLog);

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
