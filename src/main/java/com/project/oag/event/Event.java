package com.project.oag.event;

import java.time.LocalDate;
import com.project.oag.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="event")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	@Column(nullable = true)
	private String eventName;
	@Column(nullable = true)
	private String eventDescription;
	@Column(nullable = true)
	private LocalDate eventDate;
	@Column(name = "location",nullable = true)
	private String location;
	@Column(name = "capacity",nullable = true)
	private int capacity;
	@Column(name = "ticket_price",nullable = true,precision = 10)
	private double ticketPrice;
	@Lob
	@Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;
	@Column(name = "status",nullable = true)
	private String status;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	public Event() {
		super();
	}
	public Event(Long id, String eventName,
				 String eventDescription, LocalDate eventDate,
				 String location, int capacity,
				 double ticketPrice, byte[] image,
				 String status, User user) {
		this.id = id;
		this.eventName = eventName;
		this.eventDescription = eventDescription;
		this.eventDate = eventDate;
		this.location = location;
		this.capacity = capacity;
		this.ticketPrice = ticketPrice;
		this.image = image;
		this.status = status;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}