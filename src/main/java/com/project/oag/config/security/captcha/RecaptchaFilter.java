package com.project.oag.config.security.captcha;

import com.project.oag.common.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.project.oag.common.AppConstants.REGISTER_ACTION;

@Slf4j
@Service
public class RecaptchaFilter {

    private final CaptchaService recaptchaService;

    public RecaptchaFilter(CaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    //    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("POST")) {
            String response = request.getHeader("recaptcha");
            val recaptchaResponse = recaptchaService.processResponse(response, REGISTER_ACTION);
            if (!recaptchaResponse.success()) {
                log.info(AppConstants.LOG_PREFIX, "Recaptcha filter ", "Invalid reCAPTCHA token");
                throw new BadCredentialsException("Invalid reCaptcha token");
            }
        }
        filterChain.doFilter(request, httpResponse);
    }

}