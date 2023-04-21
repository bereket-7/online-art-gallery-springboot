package com.project.oag.controller.dto;

import com.project.oag.entity.Artwork;

public class ArtworkDto {

	private String artworkName;
	
	private String artworkDescription;

	private String artworkCategory;
	
	private String artworkPhoto;
	
	private int price;
	
	private String size;
	
	
	
	
    public ArtworkDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArtworkDto(String artworkName, String artworkDescription, String artworkCategory, String artworkPhoto,
			int price, String size) {
		super();
		this.artworkName = artworkName;
		this.artworkDescription = artworkDescription;
		this.artworkCategory = artworkCategory;
		this.artworkPhoto = artworkPhoto;
		this.price = price;
		this.size = size;
	}

	public ArtworkDto(Artwork product) {
        this.setArtworkName(product.getArtworkName());
        this.setArtworkPhoto(product.getArtworkPhoto());
        this.setArtworkDescription(product.getArtworkDescription());
        this.setPrice(product.getPrice());
        this.setArtworkCategory(product.getArtworkCategory());
    }
	
	public static ArtworkDto toDto(Artwork artwork) {
	    ArtworkDto artworkDTO = new ArtworkDto();
	    artworkDTO.setArtworkPhoto(artwork.getArtworkPhoto());
	    artworkDTO.setArtworkCategory(artwork.getArtworkCategory());
	    artworkDTO.setPrice(artwork.getPrice());
	    artworkDTO.setSize(artwork.getSize());
	    return artworkDTO;
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

	public String getArtworkPhoto() {
		return artworkPhoto;
	}

	public void setArtworkPhoto(String artworkPhoto) {
		this.artworkPhoto = artworkPhoto;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
}
