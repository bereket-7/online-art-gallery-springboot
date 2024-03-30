package com.project.oag.app.controller;

import com.project.oag.app.service.RegistrationRequest;
import com.project.oag.app.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(path = "api/v1/registration")
@CrossOrigin("http://localhost:8080/")
public class RegistrationController {
	@Autowired
	private RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) {
		super();
		this.registrationService = registrationService;
	}
	@PostMapping("/register")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
