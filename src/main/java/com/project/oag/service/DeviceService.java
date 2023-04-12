package com.project.oag.service;

import static java.util.Objects.nonNull;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Parser;

//@Component
public class DeviceService {
    private static final String UNKNOWN = "UNKNOWN";

    @Value("${support.email}")
    private String from;
    private Parser parser;
    private JavaMailSender mailSender;
    private MessageSource messages;

    public DeviceService(
                         Parser parser,
                         JavaMailSender mailSender,
                         MessageSource messages) {
        this.parser = parser;
        this.mailSender = mailSender;
        this.messages = messages;
    }

    private String extractIp(HttpServletRequest request) {
        String clientIp;
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (nonNull(clientXForwardedForIp)) {
            clientIp = parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    private String parseXForwardedHeader(String header) {
        return header.split(" *, *")[0];
    }

    private String getMessage(String code, Locale locale) {
        try {
            return messages.getMessage(code, null, locale);

        } catch (NoSuchMessageException ex) {
            return messages.getMessage(code, null, Locale.ENGLISH);
        }
    }
	
	
}
