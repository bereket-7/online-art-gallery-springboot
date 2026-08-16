package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class OfferResponseDto {
    private Long id;
    private Long artworkId;
    private Long buyerId;
    private BigDecimal amount;
    private OfferStatus status;
    private Timestamp createdAt;
}
