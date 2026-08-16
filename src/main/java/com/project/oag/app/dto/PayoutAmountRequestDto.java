package com.project.oag.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayoutAmountRequestDto {
    @NotNull
    private BigDecimal amount;
}
