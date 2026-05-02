package com.project.oag.app.controller;

import com.project.oag.app.dto.auth.RefreshTokenRequestDto;
import com.project.oag.app.service.auth.UserTokenService;
import com.project.oag.common.GenericResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/token")
public class RefreshTokenController {

    private final UserTokenService userTokenService;

    public RefreshTokenController(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<GenericResponse> refresh(@Valid @RequestBody RefreshTokenRequestDto requestDto) {
        return userTokenService.refreshToken(requestDto.refreshToken());
    }
}
