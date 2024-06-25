package com.project.oag.app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSearchRequestDto {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public UserSearchRequestDto() {
    }

    public UserSearchRequestDto(String uuid, String firstName, String lastName, String email, String phone, LocalDateTime fromDate, LocalDateTime toDate) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}