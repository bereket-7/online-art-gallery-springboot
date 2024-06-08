package com.project.oag.app.service.auth;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.project.oag.common.AppConstants.HEADER_AUTHORIZATION;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserTokenService tokenService;

    public JwtAuthFilter(UserTokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Intercept the request extract username, then validate the bearer token, then load the user
     * create auth token object
     * set on auth token on security context holder
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        val authHeader = request.getHeader(HEADER_AUTHORIZATION);
        tokenService.validateRequestToken(request, authHeader);
        filterChain.doFilter(request, response);
    }
}

