package com.project.oag.exceptions;

public class UpdateFailException extends IllegalArgumentException {
    public UpdateFailException(String msg) {
        super(msg);
    }
}