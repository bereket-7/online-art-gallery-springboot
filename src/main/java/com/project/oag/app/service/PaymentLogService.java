package com.project.oag.app.service;

import com.project.oag.app.entity.PaymentLog;
import com.project.oag.app.repository.PaymentLogRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentLogService {

    private final PaymentLogRepository paymentLogRepository;

    public PaymentLogService(PaymentLogRepository paymentLogRepository) {
        this.paymentLogRepository = paymentLogRepository;
    }

    public PaymentLog findByToken(String token) {
        return paymentLogRepository.findByToken(token);
    }

    public PaymentLog createPaymentLog(PaymentLog paymentLog) {
        return this.paymentLogRepository.save(paymentLog);
    }
}
