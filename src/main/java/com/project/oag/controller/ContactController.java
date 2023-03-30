package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ContactController {
	@Autowired
	private JavaMailSender mailSender;
	@GetMapping("/contact")
	public String showContactForm() {
		return "contact_form";
	}
	@PostMapping("/contact")
	public String submitContact(HttpServletRequest request){
		String fullname = request.getParameter("fullname");
		String email = request.getParameter("email");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		SimpleMailMessage message = new SimpleMailMessage();
		 message.setFrom("contact@artGallery.com");
		 message.setTo("berekethonelign2@gmail.com");
		 
		 String mailSubject = fullname + "has sent message";
		 String mailContent = "mailsender" + fullname +"\n";
		 mailContent += "sender email: " + email + "\n";
		 mailContent += "subject: " + subject + "\n";
		 mailContent += "subject: " + content + "\n";
		 
		 message.setSubject(mailSubject);
		 message.setText(mailContent);
		 
		 mailSender.send(message);
		return "message";
	}
}
