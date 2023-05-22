package com.project.oag.security;
import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@CrossOrigin("http://localhost:8080/")
@Component("LogoutSuccessHandler")
public class LogoutSuccessHandler {
	
	  //@Override
	  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	        final HttpSession session = request.getSession();
	        if (session != null) {
	            session.removeAttribute("user");
	        }

	        response.sendRedirect("http://localhost:8080?logSucc=true");
	    }

}