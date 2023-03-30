package com.project.oag.controller.dto;

import java.util.Date;

public class ArtworkDto {

	private String artworkName;
    private String artworkDescription;
    private String artworkCategory;
	private int price;
	private String artistName;
	private String size;
	private String status;
	private byte[] artworkPhoto; 
	private Date createDate;
	

	public ArtworkDto(String artworkName, String artworkDescription, String artworkCategory, int price,
			String artistName, String size, String status, byte[] artworkPhoto, Date createDate) {
		super();
		this.artworkName = artworkName;
		this.artworkDescription = artworkDescription;
		this.artworkCategory = artworkCategory;
		this.price = price;
		this.artistName = artistName;
		this.size = size;
		this.status = status;
		this.artworkPhoto = artworkPhoto;
		this.createDate = createDate;
	}

	public ArtworkDto() {
		// TODO Auto-generated constructor stub
	}

	public String getArtworkName() {
		return artworkName;
	}
	public void setArtworkName(String artworkName) {
		this.artworkName = artworkName;
	}
	public String getArtworkDescription() {
		return artworkDescription;
	}
	public void setArtworkDescription(String artworkDescription) {
		this.artworkDescription = artworkDescription;
	}
	public String getArtworkCategory() {
		return artworkCategory;
	}
	public void setArtworkCategory(String artworkCategory) {
		this.artworkCategory = artworkCategory;
	}

	public byte[] getArtworkPhoto() {
		return artworkPhoto;
	}

	public void setArtworkPhoto(byte[] artworkPhoto) {
		this.artworkPhoto = artworkPhoto;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}