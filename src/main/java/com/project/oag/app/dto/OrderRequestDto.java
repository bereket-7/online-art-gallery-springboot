package com.project.oag.app.dto;

import com.project.oag.app.model.OrderAddress;
import lombok.Data;

@Data
public class OrderRequestDto {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private OrderAddress address;
}
