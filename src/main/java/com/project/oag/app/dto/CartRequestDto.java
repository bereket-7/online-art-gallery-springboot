package com.project.oag.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequestDto {
    @NotNull
    private Long artworkId;

    @Min(1)
    private int quantity = 1;
}
