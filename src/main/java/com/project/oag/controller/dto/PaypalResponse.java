package com.project.oag.controller.dto;

public class PaypalResponse {
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

    public PaypalResponse(String sessionId) {
        this.sessionId = sessionId;
    }

    public PaypalResponse() {
    }

}
