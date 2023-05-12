package com.project.oag.controller.dto;

import java.time.LocalDate;

import org.springframework.core.io.Resource;

import com.project.oag.entity.Event;

public class EventDto {

    //private String eventPhoto; // Add a new field to hold the URL of the event photo
    //private String imageData;
    
    
        private String eventName;
        private String eventDescription;
        private LocalDate eventDate;
        private String location;
        private String capacity;
        private int ticketPrice;
        private String status;
  
        private byte[] image;

        // Getter and setter methods here..

    
  
    // Constructors
 /*   public EventDto() {
    	super();
    }
    
    public EventDto(Event event) {
        this.id = event.getId();
        this.eventName = event.getEventName();
        this.eventDescription = event.getEventDescription();
        this.eventDate = event.getEventDate();
        this.location = event.getLocation();
        this.capacity = event.getCapacity();
        this.ticketPrice = event.getTicketPrice();
        this.eventPhoto = event.getEventPhoto(); // Set the photo URL to the event's photo path
    }*/
    
    public EventDto(Event event, Resource file) {
		// TODO Auto-generated constructor stub
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

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public int getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

   
}
