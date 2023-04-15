package com.project.oag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity@
Table(name="Event")
public class Event {
	
	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String eventphoto;
	
	@Column(name = "event_name", nullable = true)
	private String eventName;
	
	@Column(name = "event_description", nullable = true)
	private String eventDescription;
	
	
	public Event(String eventphoto, String eventName, String eventDescription) {
		super();
		this.eventphoto = eventphoto;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Event() {
		super();
	}
	
	public Event(String filename, String string) {
		// TODO Auto-generated constructor stub
	}
	public String getEventphoto() {
		return eventphoto;
	}
	public void setEventphoto(String eventphoto) {
		this.eventphoto = eventphoto;
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
		
}
