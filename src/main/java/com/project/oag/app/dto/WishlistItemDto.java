package com.project.oag.app.dto;

import lombok.Data;

@Data
public class WishlistItemDto {
    private Long id;
    private ArtworkResponseDto artwork;
}
