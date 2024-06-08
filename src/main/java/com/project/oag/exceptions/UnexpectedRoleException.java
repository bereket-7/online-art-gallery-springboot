package com.project.oag.exceptions;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnexpectedRoleException extends RuntimeException {
    private int status;
    private String role;


    public UnexpectedRoleException(final HttpStatus status, final String message, final String role) {
        super(message);
        this.role = role;
        this.status = status.value();
    }

    public UnexpectedRoleException(final String message, final String role) {
        super(message);
        this.role = role;
        this.status = HttpStatus.UNAUTHORIZED.value();
    }
}
