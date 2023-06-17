package com.project.oag.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;
    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("kelem.oag.app@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }
    @Override
    public void sendEmail(EmailDetail emailDetail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //helper.setTo(emailDetail.getRecipient());
            helper.setTo("kelem.oag.app@gmail.com");
            helper.setSubject(emailDetail.getSubject());
            helper.setFrom(emailDetail.getSender());
            helper.setText(emailDetail.getMessage());

            if (emailDetail.getAttachment() != null && !emailDetail.getAttachment().isEmpty()) {
                File attachmentFile = new File(emailDetail.getAttachment());
                helper.addAttachment(attachmentFile.getName(), attachmentFile);
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
