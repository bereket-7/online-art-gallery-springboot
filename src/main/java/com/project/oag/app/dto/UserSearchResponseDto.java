package com.project.oag.app.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserSearchResponseDto {
    private String uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String image;
    private String sex;

    public UserSearchResponseDto() {
    }

    public UserSearchResponseDto(String uuid, String firstName, String lastName, String email, String phone, String image, String sex) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.sex = sex;
    }
}
