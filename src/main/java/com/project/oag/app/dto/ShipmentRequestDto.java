package com.project.oag.app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ShipmentRequestDto {
    @NotEmpty
    private String carrier;
    @NotEmpty
    private String trackingNumber;
}
