package com.project.oag.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="event")
public class Event {
	
	@Id  
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = true)
	private String eventphoto;
	
	@Column(name = "event_name", nullable = true)
	private String eventName;
	
	@Column(name = "event_description", nullable = true)
	private String eventDescription;
	
	@Column(name = "event_date", nullable = true)
	private LocalDate eventDate;
	
	@Column(name = "location",nullable = true)
	private String location;
	
	@Column(name = "capacity",nullable = true)
	private String capacity;
	
	@Column(name = "ticket_price",nullable = true)
	private int ticketPrice;
	
	@Column(name = "status",nullable = true)
	private String status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id")
	private Organization organization;
	

	public Event(Long id, String eventphoto, String eventName, String eventDescription, LocalDate eventDate,
			String location, String capacity, int ticketPrice, String status, Organization organization) {
		super();
		this.id = id;
		this.eventphoto = eventphoto;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventDate = eventDate;
		this.location = location;
		this.capacity = capacity;
		this.ticketPrice = ticketPrice;
		this.status = status;
		this.organization = organization;
	}
	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Event(String filename, String string) {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
		
}