package com.project.oag.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageBodyRequestDto {
    @NotBlank
    private String body;
}
