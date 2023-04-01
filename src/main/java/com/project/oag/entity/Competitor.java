package com.project.oag.entity;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@Lob
	@Column(name = "art", nullable = false)
    private byte[] artwork;	

	@Column(name = "art_description", nullable = false, length=15)
	private String artDescription;
	
	@OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
	private List<Vote> votes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competition_id", nullable = false)
	@JsonIgnore
	private Competition competition;
	
	public Competitor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Competitor(String firstName, String lastName, String email, String phone, byte[] artwork,
			String artDescription, List<Vote> votes, Competition competition) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.artwork = artwork;
		this.artDescription = artDescription;
		this.votes = votes;
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

	public byte[] getArtwork() {
		return artwork;
	}

	public void setArtwork(byte[] artwork) {
		this.artwork = artwork;
	}

	public String getArtDescription() {
		return artDescription;
	}


	public void setArtDescription(String artDescription) {
		this.artDescription = artDescription;
	}


	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	@Override
	public String toString() {
		return "Competitor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", artwork=" + Arrays.toString(artwork) + ", artDescription=" + artDescription
				+ ", votes=" + votes + ", competition=" + competition + "]";
	}
	
}
