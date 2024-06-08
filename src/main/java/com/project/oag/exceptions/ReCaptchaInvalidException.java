package com.project.oag.exceptions;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReCaptchaInvalidException extends RuntimeException {
    private int status;

    public ReCaptchaInvalidException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }

    public ReCaptchaInvalidException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST.value();
    }
}