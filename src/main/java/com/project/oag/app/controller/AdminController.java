package com.project.oag.app.controller;

import com.project.oag.app.dto.VerifyOtpRequestDTO;
import com.project.oag.app.dto.auth.RegisterUserRequestDto;
import com.project.oag.app.service.auth.PasswordService;
import com.project.oag.app.service.auth.RoleManagementService;
import com.project.oag.app.service.auth.UserService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final PasswordService passwordService;
    private final RoleManagementService roleManagementService;

    public AdminController(UserService userService,
                           PasswordService passwordService,
                           RoleManagementService roleManagementService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.roleManagementService = roleManagementService;
    }
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> addUser(HttpServletRequest request, @Valid @RequestBody RegisterUserRequestDto user) {
        return userService.registerUser(request, user);
    }

    @PostMapping("/register/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> userVerify(HttpServletRequest request, @RequestBody VerifyOtpRequestDTO verifyOtpRequestDTO) {
        return userService.verifyRegisterOtp(request, verifyOtpRequestDTO);
    }


}
