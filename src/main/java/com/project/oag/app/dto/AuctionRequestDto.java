package com.project.oag.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class AuctionRequestDto {
    @NotNull
    private Long artworkId;
    @NotNull
    private Timestamp startTime;
    @NotNull
    private Timestamp endTime;
    private BigDecimal reservePrice;
}
