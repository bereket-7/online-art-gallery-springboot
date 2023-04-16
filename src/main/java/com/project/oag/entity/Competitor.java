package com.project.oag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "competitor")
public class Competitor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private String artworkPhoto;

	private String artDescription;
	
	private String category;
	
	private int vote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competition_id", nullable = true)
	@JsonIgnore
	private Competition competition;

	public Competitor() {
		super();
	}

	public Competitor(String firstName, String lastName, String email, String phone, String artworkPhoto,
			String artDescription, String category, int vote, Competition competition) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.artworkPhoto = artworkPhoto;
		this.artDescription = artDescription;
		this.category = category;
		this.vote = vote;
		this.competition = competition;
	}



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

	public String getArtDescription() {
		return artDescription;
	}

	public void setArtDescription(String artDescription) {
		this.artDescription = artDescription;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}



	public String getArtworkPhoto() {
		return artworkPhoto;
	}



	public void setArtworkPhoto(String artworkPhoto) {
		this.artworkPhoto = artworkPhoto;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public int getVote() {
		return vote;
	}



	public void setVote(int vote) {
		this.vote = vote;
	}
	

}
