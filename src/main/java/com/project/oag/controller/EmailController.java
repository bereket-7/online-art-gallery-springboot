package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

	@Autowired
	private JavaMailSender mailSender;
	
	@PostMapping("/send-email")
	public ResponseEntity<?> sendEmail(
	        @RequestParam String recipient,
	        @RequestParam String subject,
	        @RequestParam String message) {

	    try {
	        SimpleMailMessage email = new SimpleMailMessage();
	        email.setTo(recipient);
	        email.setSubject(subject);
	        email.setText(message);
	        email.setFrom("");

	        mailSender.send(email);

	        return ResponseEntity.ok("Email sent successfully to " + recipient);
	    } catch (Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error sending email: " + ex.getMessage());
	    }
	}


}
