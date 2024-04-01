package com.project.oag.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * This is a custom exception which is thrown when no event number is passed in
 * HTTP URL query parameter
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GeneralException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;

    public GeneralException(String message) {
        this.message = message;
    }
}
