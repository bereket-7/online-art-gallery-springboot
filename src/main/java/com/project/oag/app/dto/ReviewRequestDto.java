package com.project.oag.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDto {
    @NotNull
    private Double rating;
    private String comment;
}
