package com.project.oag.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShipmentResponseDto {
    private Long id;
    private Long orderId;
    private String carrier;
    private String trackingNumber;
    private ShipmentStatus status;
    private Timestamp shippedAt;
    private Timestamp deliveredAt;
}
