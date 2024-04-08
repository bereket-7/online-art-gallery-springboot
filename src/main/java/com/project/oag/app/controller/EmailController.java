package com.project.oag.app.controller;

import com.project.oag.app.model.EmailDetail;
import com.project.oag.app.service.EmailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/email")
public class EmailController {
	private final EmailSender emailSender;
    public EmailController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }
    @PostMapping("/sendEmail")
	public void sendEmail(@RequestBody EmailDetail emailDetail) {
		emailSender.sendEmail(emailDetail);
	}

}
