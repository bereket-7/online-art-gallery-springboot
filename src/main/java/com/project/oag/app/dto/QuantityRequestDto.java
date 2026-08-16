package com.project.oag.app.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class QuantityRequestDto {
    @Min(1)
    private int quantity;
}
