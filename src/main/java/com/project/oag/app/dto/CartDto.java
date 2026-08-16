package com.project.oag.app.dto;

import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private ArtworkResponseDto artwork;
    private int quantity;
}
