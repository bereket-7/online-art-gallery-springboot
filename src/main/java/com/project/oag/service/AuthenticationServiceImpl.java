package com.project.oag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.configuration.MessageStrings;
import com.project.oag.entity.AuthenticationToken;
import com.project.oag.user.User;
import com.project.oag.exceptions.AuthenticationFailException;
import com.project.oag.repository.TokenRepository;
import com.project.oag.common.Helper;


@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	 	@Autowired
	    private TokenRepository repository;
	 	@Override
	    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
	        repository.save(authenticationToken);
	    }
	    public AuthenticationToken getToken(User user) {
	        return repository.findTokenByUser(user);
	    }
	    @Override
	    public User getUser(String token) {
	        AuthenticationToken authenticationToken = repository.findTokenByToken(token);
	        if (Helper.notNull(authenticationToken)) {
	            if (Helper.notNull(authenticationToken.getUser())) {
	                return authenticationToken.getUser();
	            }
	        }
	        return null;
	    }
	    @Override
	    public void authenticate(String token) throws AuthenticationFailException {
	        if (!Helper.notNull(token)) {
	            throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
	        }
	        if (!Helper.notNull(getUser(token))) {
	            throw new AuthenticationFailException(MessageStrings.AUTH_TOEKN_NOT_VALID);
	        }
	    }
}
