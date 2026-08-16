package com.project.oag.app.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String checkOutUrl;
    private String txRef;
    @JsonIgnore
    private com.project.oag.app.entity.PaymentLog paymentLog;
}
