package com.project.oag.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.oag.app.dto.NotificationChannel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthRequestDto(
        @NotEmpty(message = "Username/Email field can not be empty.")
        @Email
        @JsonProperty("username")
        String username,
        @NotEmpty(message = "Password field can not be empty.")
        @JsonProperty("password")
        String password,
        @NotNull(message = "Channel field can not be empty.")
        @JsonProperty("channel")
        NotificationChannel channel
) {
}