package com.project.oag.security;
import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component("LogoutSuccessHandler")
public class LogoutSuccessHandler {
	
	  //@Override
	  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	        final HttpSession session = request.getSession();
	        if (session != null) {
	            session.removeAttribute("user");
	        }

	        response.sendRedirect("/logout.html?logSucc=true");
	    }

}
