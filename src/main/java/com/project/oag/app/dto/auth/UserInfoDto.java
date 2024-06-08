package com.project.oag.app.dto.auth;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record UserInfoDto(
        String uuid,
        String token,
        String username,
        List<String> permissions,
        String fullName,
        String avatarUrl
) implements Serializable {
}
