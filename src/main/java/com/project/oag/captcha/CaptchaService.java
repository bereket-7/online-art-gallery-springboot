package com.project.oag.captcha;

import com.project.oag.exceptions.ReCaptchaInvalidException;

public interface CaptchaService {
	  
    default void processResponse(final String response) throws ReCaptchaInvalidException {}
    
    default void processResponse(final String response, String action) throws ReCaptchaInvalidException {}
    
    String getReCaptchaSite();

    String getReCaptchaSecret();

}
