package com.project.oag.app.dto;

import lombok.Data;

@Data
public class OrderAddressDto {
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
