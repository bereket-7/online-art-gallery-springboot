package com.project.oag.app.model;

public class EmailDetail {
    private String recipient;
    private String message;
    private String subject;
    private String attachment;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	private String sender;
    
	public EmailDetail() {
		super();
	}
	public EmailDetail(String recipient, String message, String subject, String attachment) {
		super();
		this.recipient = recipient;
		this.message = message;
		this.subject = subject;
		this.attachment = attachment;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
    
}
