package com.project.oag.app.dto.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import static com.project.oag.common.AppConstants.PASSWORD_PATTERN;

public record PasswordResetRequest(@JsonProperty("otp")
                                   @NotEmpty
                                   String otp,
                                   @JsonProperty("email")
                                   @NotEmpty
                                   String email,
                                   @JsonProperty("password")
                                   @NotEmpty(message = "Password field can not be empty.")
                                   @Pattern(regexp = PASSWORD_PATTERN)
                                   String password,
                                   @JsonProperty("confirmPassword")
                                   @NotEmpty(message = "Confirm password field can not be empty.")
                                   @Pattern(regexp = PASSWORD_PATTERN) String confirmPassword) {
    @AssertTrue(message = "Password and Confirm password fields should be the same")
    public boolean isPasswordMatch() {
        return password.equals(confirmPassword);
    }
}
