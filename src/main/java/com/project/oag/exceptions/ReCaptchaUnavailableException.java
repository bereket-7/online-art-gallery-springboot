package com.project.oag.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
public class ReCaptchaUnavailableException extends RuntimeException {
    private int status;

    public ReCaptchaUnavailableException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }

    public ReCaptchaUnavailableException(String message) {
        super(message);
        this.status = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value();
    }
}