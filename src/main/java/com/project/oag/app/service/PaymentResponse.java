package com.project.oag.app.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String checkOutUrl;
    private String txRef;
    private com.project.oag.app.entity.PaymentLog paymentLog;
}
