package com.project.oag.app.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRequestDto {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String sex;
    private Integer age;
    private String username;
    private String password;
    private Role role;
}