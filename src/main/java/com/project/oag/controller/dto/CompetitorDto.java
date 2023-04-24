package com.project.oag.controller.dto;

public class CompetitorDto {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String artworkPhoto;
	private String artDescription;
	private String category;
	
	public CompetitorDto(String firstName, String lastName, String email, String phone, String artworkPhoto,
			String artDescription, String category) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.artworkPhoto = artworkPhoto;
		this.artDescription = artDescription;
		this.category = category;
	}

	public CompetitorDto() {
		super();
		// TODO Auto-generated constructor stub
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
	
	
}
