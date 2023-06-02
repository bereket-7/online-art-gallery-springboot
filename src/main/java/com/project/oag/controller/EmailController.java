package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.EmailDetail;
import com.project.oag.service.EmailService;
@RestController()
@RequestMapping("/email")
@CrossOrigin("http://localhost:8080/")
public class EmailController {
	
	   @Autowired 
	   private EmailService emailService;
	
    // Sending a simple Email
    @PostMapping("/sendEmail")
    public String
    sendMail(@RequestBody EmailDetail details)
    {
        String status= emailService.sendSimpleMail(details);
        return status;
    }
 
    // Sending email with attachment
    @PostMapping("/sendWithAttachment")
    public String sendMailWithAttachment(
        @RequestBody EmailDetail details)
    {
        String status= emailService.sendMailWithAttachment(details);
        return status;
    }

}
