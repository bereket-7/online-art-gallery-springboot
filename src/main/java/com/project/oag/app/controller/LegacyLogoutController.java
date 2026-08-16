package com.project.oag.app.controller;

import com.project.oag.common.GenericResponse;
import com.project.oag.config.security.LogoutHandlerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
public class LegacyLogoutController {
    private final LogoutHandlerService logoutHandlerService;

    public LegacyLogoutController(LogoutHandlerService logoutHandlerService) {
        this.logoutHandlerService = logoutHandlerService;
    }

    @PostMapping("/api/v1/logout")
    public ResponseEntity<GenericResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutHandlerService.logout(request, response, null);
        return prepareResponse(HttpStatus.OK, "Logout successful", Collections.emptyList());
    }
}
