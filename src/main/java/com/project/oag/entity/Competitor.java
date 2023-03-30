package com.project.oag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="competitor")
public class Competitor {
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "first_name", nullable = false, length=100)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length=100)
	private String lastName;
	
	@Column(name = "email",nullable = false)
	private String email;
	
	@Column(name = "phone", nullable = false, length=15)
	private String phone;
	
	@Column(name = "artwork_photo", nullable = false)
    private String artworkPhoto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competition_id", nullable = false)
	@JsonIgnore
	private Competition competition;
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getArtworkPhoto() {
		return artworkPhoto;
	}

	public void setArtworkPhoto(String artworkPhoto) {
		this.artworkPhoto = artworkPhoto;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	
}
