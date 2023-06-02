package com.project.oag.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="bidArt")
public class BidArt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    private String artist;
	@Lob
	@Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;
    private String description;
    private Double initialAmount;
    private LocalDateTime bidEndTime;
    private LocalDateTime startingTime;
	private boolean biddingStarted;
	@OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();
	public List<Bid> getBids() {
		return bids;
	}
	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(Double initialAmount) {
		this.initialAmount = initialAmount;
	}

	public LocalDateTime getBidEndTime() {
		return bidEndTime;
	}

	public void setBidEndTime(LocalDateTime bidEndTime) {
		this.bidEndTime = bidEndTime;
	}

	public LocalDateTime getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(LocalDateTime startingTime) {
		this.startingTime = startingTime;
	}

	public boolean isBiddingStarted() {
		return biddingStarted;
	}

	public void setBiddingStarted(boolean biddingStarted) {
		this.biddingStarted = biddingStarted;
	}
    
}
