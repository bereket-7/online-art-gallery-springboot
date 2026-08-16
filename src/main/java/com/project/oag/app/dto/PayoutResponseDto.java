package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class PayoutResponseDto {
    private Long id;
    private Long artistId;
    private BigDecimal amount;
    private PayoutStatus status;
    private Timestamp requestedAt;
    private Timestamp processedAt;
    private String externalRef;
    private boolean manual;
}
