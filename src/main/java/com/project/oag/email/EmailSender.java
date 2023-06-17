package com.project.oag.email;

public interface EmailSender {
    void send(String to, String email);
    void sendEmail(EmailDetail emailDetail);
}