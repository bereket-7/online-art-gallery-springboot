package com.project.oag.service;

import com.project.oag.entity.EmailDetail;


public interface EmailService {
    String sendSimpleMail(EmailDetail details);
    String sendMailWithAttachment(EmailDetail details);
}
/**
	@Autowired
    private JavaMailSender javaMailSender;
 
    @Value("${spring.mail.username}")
    private String emailFrom;
 
    @Value("${app.url}")
    private String appUrl;
 
    public void sendEmailVerification(String email, String token) {
        String subject = "Account Verification - Sheba Online Art Gallery";
        String message = "Please click on below link to verify your email:\n"
                + appUrl + "/verify-email?token=" + token;
        sendEmail(email, subject, message);
    }
 
    private void sendEmail(String email, String subject, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(emailFrom);
        mail.setSubject(subject);
        mail.setText(message);
 
        javaMailSender.send(mail);
    }**/

