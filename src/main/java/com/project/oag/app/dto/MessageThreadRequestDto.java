package com.project.oag.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageThreadRequestDto {
    @NotNull
    private Long artistId;
    private Long artworkId;
    @NotBlank
    private String body;
}
