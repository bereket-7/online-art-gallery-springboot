package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.oag.captcha.CaptchaService;
import com.project.oag.captcha.CaptchaServiceV3Impl;
import com.project.oag.common.GenericResponse;
import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.User;
import com.project.oag.registration.OnRegistrationCompleteEvent;
import com.project.oag.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class RegistrationCaptchaController {
	
	    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	    @Autowired
	    private UserService userService;

	    @Autowired
	    private CaptchaService captchaService;
	    
	    @Autowired
	    private CaptchaService captchaServiceV3;

	    @Autowired
	    private ApplicationEventPublisher eventPublisher;

	    public RegistrationCaptchaController() {
	        super();
	    }

	    // Registration
	    @PostMapping("/user/registrationCaptcha")
	    public GenericResponse captchaRegisterUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {

	        final String response = request.getParameter("g-recaptcha-response");
	        captchaService.processResponse(response);

	        return registerNewUserHandler(accountDto, request);
	    }

	    
	    // Registration reCaptchaV3
	    @PostMapping("/user/registrationCaptchaV3")
	    public GenericResponse captchaV3RegisterUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {

	        final String response = request.getParameter("response");
	        captchaServiceV3.processResponse(response, CaptchaServiceV3Impl.REGISTER_ACTION);

	        return registerNewUserHandler(accountDto, request);
	    }
	    
	    private GenericResponse registerNewUserHandler(final UserDto userDto, final HttpServletRequest request) {
	        LOGGER.debug("Registering user account with information: {}", userDto);

	        final User registered = userService.registerNewUser(userDto);
	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
	        return new GenericResponse("success");
	    }
	    

	    private String getAppUrl(HttpServletRequest request) {
	        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	    }

	}
	

