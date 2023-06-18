package com.project.oag.service;

import com.project.oag.entity.AuthenticationToken;
import com.project.oag.user.User;
import com.project.oag.exceptions.AuthenticationFailException;

public interface AuthenticationService {

	void saveConfirmationToken(AuthenticationToken authenticationToken);

	User getUser(String token);

	void authenticate(String token) throws AuthenticationFailException;
	
}
