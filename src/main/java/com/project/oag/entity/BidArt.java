package com.project.oag.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="bidArt")
public class BidArt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String artName;
    
    private String Artist;
    
    private String artDescription;
    
    private BigDecimal initialAmount;
    
    private LocalDateTime biddingStartTime;
    
    private LocalDateTime biddingEndTime;

	public BidArt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BidArt(String artName, String artist, String artDescription, BigDecimal initialAmount,
			LocalDateTime biddingStartTime, LocalDateTime biddingEndTime) {
		super();
		this.artName = artName;
		Artist = artist;
		this.artDescription = artDescription;
		this.initialAmount = initialAmount;
		this.biddingStartTime = biddingStartTime;
		this.biddingEndTime = biddingEndTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArtName() {
		return artName;
	}

	public void setArtName(String artName) {
		this.artName = artName;
	}

	public String getArtist() {
		return Artist;
	}

	public void setArtist(String artist) {
		Artist = artist;
	}

	public String getArtDescription() {
		return artDescription;
	}

	public void setArtDescription(String artDescription) {
		this.artDescription = artDescription;
	}

	public BigDecimal getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(BigDecimal initialAmount) {
		this.initialAmount = initialAmount;
	}

	public LocalDateTime getBiddingStartTime() {
		return biddingStartTime;
	}

	public void setBiddingStartTime(LocalDateTime biddingStartTime) {
		this.biddingStartTime = biddingStartTime;
	}

	public LocalDateTime getBiddingEndTime() {
		return biddingEndTime;
	}

	public void setBiddingEndTime(LocalDateTime biddingEndTime) {
		this.biddingEndTime = biddingEndTime;
	}
    

}
