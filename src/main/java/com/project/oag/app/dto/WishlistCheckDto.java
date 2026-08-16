package com.project.oag.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistCheckDto {
    private boolean inWishlist;
    private Long id;
}
