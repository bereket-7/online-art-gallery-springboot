package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long artworkId;
    private Long artistId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private String artworkName;
    private String imageUrl;
}
