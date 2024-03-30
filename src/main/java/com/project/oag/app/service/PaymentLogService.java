package com.project.oag.app.service;

import com.project.oag.app.model.PaymentLog;
import com.project.oag.app.repository.PaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentLogService {
    @Autowired
    private PaymentLogRepository paymentLogRepository;

    public PaymentLogService(PaymentLogRepository paymentLogRepository) {
        this.paymentLogRepository = paymentLogRepository;
    }

    public PaymentLog findByToken(String token) {
        return paymentLogRepository.findByToken(token);
    }
    public PaymentLog createPaymentLog(PaymentLog paymentLog){
     return this.paymentLogRepository.save(paymentLog);
 }
}
