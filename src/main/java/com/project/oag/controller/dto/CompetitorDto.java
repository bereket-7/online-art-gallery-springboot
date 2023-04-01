package com.project.oag.controller.dto;

public class CompetitorDto {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private byte[] artwork;
	private String artDescription;
	public CompetitorDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CompetitorDto(String firstName, String lastName, String email, String phone, byte[] artwork,
			String artDescription) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.artwork = artwork;
		this.artDescription = artDescription;
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
	
}
