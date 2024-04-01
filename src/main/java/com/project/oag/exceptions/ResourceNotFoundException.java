package com.project.oag.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private int status;

    public ResourceNotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND.value();
    }
}
