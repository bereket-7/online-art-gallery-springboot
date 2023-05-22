package com.project.oag.security;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.project.oag.entity.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@CrossOrigin("http://localhost:8080/")
public class CustomLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	    @Autowired
	    ActiveUserStore activeUserStore;

	    @Override
	    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
	        addWelcomeCookie(getUserName(authentication), response);
	        redirectStrategy.sendRedirect(request, response, "http://localhost:8080/?user=" + authentication.getName());

	        final HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.setMaxInactiveInterval(30 * 60);
	            String username;
	            if (authentication.getPrincipal() instanceof User) {
	            	username = ((User)authentication.getPrincipal()).getEmail();
	            }
	            else {
	            	username = authentication.getName();
	            }
	       
	            LoggedUser user = new LoggedUser(username, activeUserStore);
	            session.setAttribute("user", user);
	        }
	        clearAuthenticationAttributes(request);
	    }
	    
	    private String getUserName(final Authentication authentication) {
	        return ((User) authentication.getPrincipal()).getFirstname();
	    }

	    private void addWelcomeCookie(final String user, final HttpServletResponse response) {
	        Cookie welcomeCookie = getWelcomeCookie(user);
	        response.addCookie(welcomeCookie);
	    }

	    private Cookie getWelcomeCookie(final String user) {
	        Cookie welcomeCookie = new Cookie("welcome", user);
	        welcomeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
	        return welcomeCookie;
	    }

	    protected void clearAuthenticationAttributes(final HttpServletRequest request) {
	        final HttpSession session = request.getSession(false);
	        if (session == null) {
	            return;
	        }
	        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	    }

	    public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
	        this.redirectStrategy = redirectStrategy;
	    }

	    protected RedirectStrategy getRedirectStrategy() {
	        return redirectStrategy;
	    }
}
