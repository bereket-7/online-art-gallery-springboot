package com.project.oag.app.controller;

import com.project.oag.app.dto.VerifyOtpRequestDTO;
import com.project.oag.app.dto.auth.AuthRequestDto;
import com.project.oag.app.dto.auth.PasswordForgotRequest;
import com.project.oag.app.dto.auth.PasswordResetRequest;
import com.project.oag.app.dto.auth.UserType;
import com.project.oag.app.service.auth.PasswordService;
import com.project.oag.app.service.auth.UserService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminAccountController {
    private final UserService userService;
    private final PasswordService passwordService;

    public AdminAccountController(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> authenticateAndGetToken(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthRequestDto authRequestDto) throws ServletException, IOException {
        return userService.authenticateUserCredentials(request, response, authRequestDto, UserType.ADMIN);
    }

    @PostMapping("/login/verify")
    public ResponseEntity<GenericResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequestDTO verifyOtpRequestDTO) {
        return userService.verifyLoginOtp(verifyOtpRequestDTO);
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<GenericResponse> forgotPassword(@RequestBody PasswordForgotRequest passwordForgotRequest) {
        return passwordService.forgotPassword(passwordForgotRequest);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<GenericResponse> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        return passwordService.resetPassword(passwordResetRequest);
    }
}
