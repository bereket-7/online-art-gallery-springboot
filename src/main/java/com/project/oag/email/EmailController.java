package com.project.oag.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/email")
@CrossOrigin("http://localhost:8080/")
public class EmailController {
	@Autowired
	private EmailSender emailSender;

	@PostMapping("/sendEmail")
	public void sendEmail(@RequestBody EmailDetail emailDetail) {
		emailSender.sendEmail(emailDetail);
	}

}
