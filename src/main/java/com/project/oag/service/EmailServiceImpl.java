package com.project.oag.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.project.oag.entity.EmailDetail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired 
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") 
    private String sender;
    public String sendSimpleMail(EmailDetail details)
    {
        try {
            SimpleMailMessage mailMessage= new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMessage());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            return "Email Sent Successfully";
        }
        catch (Exception e) {
            return "Error while Sending Email";
        }
    }

    public String sendMailWithAttachment(EmailDetail details)
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper= new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMessage());
            mimeMessageHelper.setSubject(details.getSubject());
            FileSystemResource file= new FileSystemResource(
            new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);
            javaMailSender.send(mimeMessage);
            return "Email sent Successfully.";
        }
        catch (MessagingException e) {
            return "Error while sending Email!!!";
        }
    }
}
