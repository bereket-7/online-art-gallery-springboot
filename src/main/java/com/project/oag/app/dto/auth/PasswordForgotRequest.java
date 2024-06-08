package com.project.oag.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record PasswordForgotRequest(@JsonProperty("email") @NotEmpty String email,
                                    @JsonProperty("resendOtp") boolean resendOtp) {
}

