package com.project.oag.config.security;

import com.project.oag.app.repository.TokenRepository;
import com.project.oag.app.service.auth.JwtService;
import com.project.oag.common.service.UserHelperService;
import com.project.oag.exceptions.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static com.project.oag.common.AppConstants.BEARER;
import static com.project.oag.common.AppConstants.HEADER_AUTHORIZATION;


@Service
@Slf4j
public class LogoutHandlerService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final UserHelperService userHelperService;
    private final JwtService jwtService;


    public LogoutHandlerService(TokenRepository tokenRepository, UserHelperService userHelperService, JwtService jwtService) {
        this.tokenRepository = tokenRepository;
        this.userHelperService = userHelperService;
        this.jwtService = jwtService;
    }

    /**
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Started logout process");
        val authHeader = request.getHeader(HEADER_AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            val token = authHeader.substring(BEARER.length());
            val username = jwtService.extractUsername(token);
            log.info("Started logout process for user: " + username);
            val userId = userHelperService.getUserByUsername(username)
                    .getId();
            var storedToken = tokenRepository.findByTokenAndUserId(token, userId)
                    .orElse(null);
            if (storedToken != null) {
                storedToken.setRevoked(true);
                storedToken.setExpired(true);
                tokenRepository.save(storedToken);
            } else {
                throw new GeneralException("Logout process failed, " + "No active token found for logout process.");
            }
        }
    }
}
