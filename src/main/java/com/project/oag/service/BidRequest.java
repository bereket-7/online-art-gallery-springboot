package com.project.oag.service;

import java.math.BigDecimal;
public class BidRequest {
    private Long artworkId;
    private Long userId;
    private BigDecimal amount;

    public BidRequest() {
    }
    public BidRequest(Long artworkId, Long userId, BigDecimal amount) {
        this.artworkId = artworkId;
        this.userId = userId;
        this.amount = amount;
    }

    public Long getArtworkId() {
        return artworkId;
    }
    public void setArtworkId(Long artworkId) {
        this.artworkId = artworkId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
