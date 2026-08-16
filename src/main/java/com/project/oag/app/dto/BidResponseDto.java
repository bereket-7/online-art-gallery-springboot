package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class BidResponseDto {
    private Long id;
    private Long auctionId;
    private Long bidderId;
    private BigDecimal amount;
    private Timestamp bidTime;
}
