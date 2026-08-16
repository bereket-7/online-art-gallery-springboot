package com.project.oag.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingRequestDto {
    @NotNull
    private Long artworkId;
    @NotNull
    private Double ratingValue;
    private String comment;
}
