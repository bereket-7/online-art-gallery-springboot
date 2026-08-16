package com.project.oag.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OfferStatusRequestDto {
    @NotNull
    private OfferStatus status;
}
