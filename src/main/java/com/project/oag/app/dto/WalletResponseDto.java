package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResponseDto {
    private Long id;
    private Long artistId;
    private BigDecimal balance;
    private BigDecimal pendingBalance;
    private Long version;
}
