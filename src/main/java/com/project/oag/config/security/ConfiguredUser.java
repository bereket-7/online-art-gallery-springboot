package com.project.oag.config.security;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ConfiguredUser(@NotEmpty String firstName,
                             @NotEmpty String lastName,
                             @NotEmpty String email,
                             @NotEmpty String password,
                             List<String> role,
                             boolean verified) {
}
