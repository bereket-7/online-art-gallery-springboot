package com.project.oag.controller;

public class LoginResponse {
    private boolean success;
    private String message;
        
	public LoginResponse() {
		super();
	}
	public LoginResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}