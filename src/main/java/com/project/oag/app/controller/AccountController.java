package com.project.oag.app.controller;

import com.project.oag.app.dto.VerifyOtpRequestDTO;
import com.project.oag.app.dto.auth.*;
import com.project.oag.app.service.auth.PasswordService;
import com.project.oag.app.service.auth.UserService;
import com.project.oag.common.GenericResponse;
import com.project.oag.config.security.LogoutHandlerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AccountController {
    private final UserService userService;
    private final PasswordService passwordService;
    private final LogoutHandlerService logoutHandlerService;

    public AccountController(UserService userService, PasswordService passwordService,
                             LogoutHandlerService logoutHandlerService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.logoutHandlerService = logoutHandlerService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> authenticateAndGetToken(HttpServletRequest request, HttpServletResponse response,
            @Valid @RequestBody AuthRequestDto authRequestDto) throws ServletException, IOException {
        return userService.authenticateUserCredentials(request, response, authRequestDto, UserType.ANY);
    }

    @PostMapping("/login/verify")
    public ResponseEntity<GenericResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequestDTO verifyOtpRequestDTO) {
        return userService.verifyLoginOtp(verifyOtpRequestDTO);
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<GenericResponse> forgotPassword(@Valid @RequestBody PasswordForgotRequest passwordForgotRequest) {
        return passwordService.forgotPassword(passwordForgotRequest);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<GenericResponse> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        return passwordService.resetPassword(passwordResetRequest);
    }

    @PostMapping("/customer/signup")
    public ResponseEntity<GenericResponse> singUp(HttpServletRequest request, @Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.customerSignup(request, signupRequestDto);
    }

    @PostMapping("/customer/signup/verify")
    public ResponseEntity<GenericResponse> verifySignupOtp(HttpServletRequest request, @Valid @RequestBody VerifyOtpRequestDTO ver) {
        return userService.verifySignupOtp(request, ver);
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> register(HttpServletRequest request, @Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.customerSignup(request, signupRequestDto);
    }

    @PostMapping("/register/verify")
    public ResponseEntity<GenericResponse> verifyRegister(HttpServletRequest request, @Valid @RequestBody VerifyOtpRequestDTO ver) {
        return userService.verifySignupOtp(request, ver);
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutHandlerService.logout(request, response, null);
        return prepareResponse(HttpStatus.OK, "Logout successful", Collections.emptyList());
    }
}
