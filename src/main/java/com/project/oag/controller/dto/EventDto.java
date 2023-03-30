package com.project.oag.controller.dto;

import java.time.LocalDateTime;

public class EventDto {
	
	private String eventName;
	private String eventDescription;
	private byte[] eventPhoto;
	private LocalDateTime timestamp;
	
	public EventDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventDto(String eventName, String eventDescription, byte[] eventPhoto, LocalDateTime timestamp) {
		super();
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventPhoto = eventPhoto;
		this.timestamp = timestamp;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	public byte[] getEventPhoto() {
		return eventPhoto;
	}

	public void setEventPhoto(byte[] eventPhoto) {
		this.eventPhoto = eventPhoto;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}
