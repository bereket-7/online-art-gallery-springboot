package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.User;
import com.project.oag.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
@Autowired
private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null || !((User) user).getPassword().equals(loginRequest.getPassword())) {
            // authentication failed
            return new ResponseEntity<>(new LoginResponse(false, "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }

        // authentication successful
        return new ResponseEntity<>(new LoginResponse(true, "Logged in successfully"), HttpStatus.OK);
    }
}

