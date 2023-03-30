package com.project.oag.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name="event")
public class Event {

	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eventId;
	
	@Column(name = "event_name", nullable = false)
	private String eventName;
	
	@Column(name = "event_description", nullable = false)
	private String eventDescription;
	
	@Lob
	@Column(name = "event_photo", nullable = false)
    private byte[] eventPhoto;

	@Column(name = "upload_time")
	private LocalDateTime timestamp;

	public Event() {
		super();
	}

	public Event(String eventName, String eventDescription, byte[] eventPhoto, LocalDateTime timestamp) {
		super();
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventPhoto = eventPhoto;
		this.timestamp = timestamp;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
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
