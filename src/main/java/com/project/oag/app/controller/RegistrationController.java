package com.project.oag.app.controller;

import com.project.oag.app.dto.UserRequestDto;
import com.project.oag.app.service.RegistrationService;
import com.project.oag.common.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(path = "api/v1/user/registration")
public class RegistrationController {
	@Autowired
	private RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) {
		super();
		this.registrationService = registrationService;
	}
	@PostMapping("/register")
    public ResponseEntity<GenericResponse> register(@RequestBody UserRequestDto userRequestDto) {
        return registrationService.register(userRequestDto);
    }
    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
