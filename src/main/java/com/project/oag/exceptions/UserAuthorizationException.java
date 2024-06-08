package com.project.oag.exceptions;

public class UserAuthorizationException extends RuntimeException {

    public UserAuthorizationException() {
        super("User is not authorized");
    }

    public UserAuthorizationException(String message) {
        super(message);
    }
}
