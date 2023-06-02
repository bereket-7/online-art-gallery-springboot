package com.project.oag.security;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.PasswordResetToken;
import com.project.oag.repository.PasswordResetTokenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserSecurityServiceImpl implements UserSecurityService {
	  @Autowired
	  private PasswordResetTokenRepository passwordTokenRepository;
	    @Override
	    public String validatePasswordResetToken(String token) {
	        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

	        return !isTokenFound(passToken) ? "invalidToken"
	                : isTokenExpired(passToken) ? "expired"
	                : null;
	    }

	    private boolean isTokenFound(PasswordResetToken passToken) {
	        return passToken != null;
	    }

	    private boolean isTokenExpired(PasswordResetToken passToken) {
	        final Calendar cal = Calendar.getInstance();
	        return passToken.getExpiryDate().before(cal.getTime());
	    }

}
