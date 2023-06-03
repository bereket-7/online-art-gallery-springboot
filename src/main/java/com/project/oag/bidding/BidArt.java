package com.project.oag.bidding;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.oag.bidding.Bid;
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
    private BigDecimal initialAmount;
    private LocalDateTime bidEndTime;
    private LocalDateTime startingTime;
	private boolean biddingStarted;
	private boolean biddingClosed;
	@OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();
	public List<Bid> getBids() {
		return bids;
	}
	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	public boolean isBiddingClosed() {
		return biddingClosed;
	}
	public void setBiddingClosed(boolean biddingClosed) {
		this.biddingClosed = biddingClosed;
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

	public BigDecimal getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(BigDecimal initialAmount) {
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
