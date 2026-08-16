package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class AuctionResponseDto {
    private Long id;
    private Long artworkId;
    private ArtworkResponseDto artwork;
    private Timestamp startTime;
    private Timestamp endTime;
    private BigDecimal reservePrice;
    private BigDecimal currentBid;
    private AuctionStatus status;
    private Long winnerId;
    private Long version;
}
