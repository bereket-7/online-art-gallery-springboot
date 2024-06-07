package com.project.oag.app.service;

import com.project.oag.app.entity.EmailDetail;

public interface EmailSender {
    void send(String to, String email);

    void sendEmail(EmailDetail emailDetail);
}