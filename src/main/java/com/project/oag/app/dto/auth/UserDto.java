package com.project.oag.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDto {
    @Transient
    @JsonIgnore
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String uuid;
    private String phone;
    private String address;
    private String sex;
    private Integer age;
    private String username;
    private String password;
    private Boolean locked;
    private Boolean enabled;
    private UserRoleDto userRole;
    private String image;
    private boolean selectedForBid;
    private String secret;
    private boolean verified;
    private boolean deleted;
    private boolean enable2FA;
    private String resetPasswordToken;
    private int loginAttemptedNumber;
    private Timestamp blockedUntil;
    private Timestamp tokenCreationTime;
    private Timestamp creationDate;
    private Timestamp lastUpdateDate;
}
