package com.project.oag.security.config;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.oag.registration.token.ConfirmationToken;
import com.project.oag.registration.token.ConfirmationTokenRepository;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	 private final ConfirmationTokenRepository confirmationTokenRepository;

	    public void saveConfirmationToken(ConfirmationToken token) {
	        confirmationTokenRepository.save(token);
	    }

	    public Optional<ConfirmationToken> getToken(String token) {
	        return confirmationTokenRepository.findByToken(token);
	    }

	    public int setConfirmedAt(String token) {
	        return confirmationTokenRepository.updateConfirmedAt(
	                token, LocalDateTime.now());
	    }
	
}
