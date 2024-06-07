package com.project.oag.app.dto;

import com.project.oag.app.entity.Artwork;
import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private Artwork artwork;
    private int quantity;
}
