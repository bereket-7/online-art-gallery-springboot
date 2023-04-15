package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Customer;
import com.project.oag.service.CustomerService;

@RestController
@RequestMapping("/customer")
@CrossOrigin("http://localhost:8080/")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer user) {
        customerService.registerUser(user);
        return ResponseEntity.ok("User registered successfully. Please check your email for confirmation.");
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam String email, @RequestParam String token) {
        customerService.confirmRegistration(email, token);
        return ResponseEntity.ok("Registration confirmed successfully.");
    }
    
}
