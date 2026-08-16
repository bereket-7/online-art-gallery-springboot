package com.project.oag.app.dto;

import com.project.oag.app.service.PaymentResponse;
import lombok.Data;

@Data
public class OfferAcceptResponseDto {
    private OfferResponseDto offer;
    private Long orderId;
    private PaymentResponse payment;
}
