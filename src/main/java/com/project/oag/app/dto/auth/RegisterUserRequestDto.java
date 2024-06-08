package com.project.oag.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.oag.app.dto.NotificationChannel;
import jakarta.validation.constraints.*;
import lombok.Data;

import static com.project.oag.common.AppConstants.*;

@Data
public class RegisterUserRequestDto {
    @NotEmpty(message = "First name field can not be empty.")
    @Pattern(regexp = NAME_PATTERN)
    @JsonProperty("firstName")
    private String firstName;

    @NotEmpty(message = "Last name field can not be empty.")
    @Pattern(regexp = NAME_PATTERN)
    @JsonProperty("lastName")
    private String lastName;

    @NotEmpty(message = "Email field can not be empty.")
    @Email
    @JsonProperty("email")
    private String email;

    @NotEmpty(message = "Password field can not be empty.")
    @Pattern(regexp = PASSWORD_PATTERN)
    @JsonProperty("password")
    private String password;

    @NotEmpty(message = "Confirm password field can not be empty.")
    @Pattern(regexp = PASSWORD_PATTERN)
    @JsonProperty("confirmPassword")
    private String confirmPassword;

    @NotEmpty(message = "Phone field can not be empty.")
    @Pattern(regexp = PHONE_PATTERN)
    @Min(value = 10, message = "Phone number should be a minimum of 10 digits.")
    @JsonProperty("phone")
    private String phone;

    @NotNull(message = "Role Id field can not be empty.")
    @JsonProperty("roleId")
    private Long roleId;

    @NotNull(message = "Channel has to be specified.")
    @JsonProperty("channel")
    private NotificationChannel channel;

    @AssertTrue(message = "Password and Confirm password fields should be the same")
    @JsonIgnore
    public boolean isPasswordMatch() {
        return password.equals(confirmPassword);
    }
}
