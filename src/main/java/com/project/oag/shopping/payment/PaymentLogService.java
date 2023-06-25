package com.project.oag.shopping.payment;

import com.project.oag.report.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentLogService {
    @Autowired
    private PaymentLogRepository paymentLogRepository;
    public PaymentLog createPaymentLog(PaymentLog paymentLog){
     return this.paymentLogRepository.save(paymentLog);
 }

}
