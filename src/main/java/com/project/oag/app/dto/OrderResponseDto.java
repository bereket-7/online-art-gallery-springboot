package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Timestamp orderDate;
    private String secretCode;
    private List<OrderItemResponseDto> items = new ArrayList<>();
    private OrderAddressDto address;
    private ShipmentResponseDto shipment;
    private List<CertificateResponseDto> certificates = new ArrayList<>();
}
