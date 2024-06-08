package com.project.oag.config.security.captcha;

import com.project.oag.exceptions.ReCaptchaInvalidException;

public interface CaptchaServiceInterface {

    RecaptchaResponse processResponse(final String response) throws ReCaptchaInvalidException;

    RecaptchaResponse processResponse(final String response, final String action) throws ReCaptchaInvalidException;

    String getReCaptchaSite();

    String getReCaptchaSecret();
}
