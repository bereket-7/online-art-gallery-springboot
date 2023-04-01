package com.project.oag.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name="bid")
public class Bid {
	
	   	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "art_id")
	    private BidArt bidArt;  
	    
	    @Column(name = "amount")
	    private BigDecimal amount;

	    @Column(nullable = false)
	    private LocalDateTime timestamp;
	
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "bidder_id", referencedColumnName = "id")
	    private Bidder bidder;

		public Bid() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Bid(BidArt bidArt, BigDecimal amount, LocalDateTime timestamp, Bidder bidder) {
			super();
			this.bidArt = bidArt;
			this.amount = amount;
			this.timestamp = timestamp;
			this.bidder = bidder;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public BidArt getBidArt() {
			return bidArt;
		}

		public void setBidArt(BidArt bidArt) {
			this.bidArt = bidArt;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public LocalDateTime getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}

		public Bidder getBidder() {
			return bidder;
		}

		public void setBidder(Bidder bidder) {
			this.bidder = bidder;
		}

		@Override
		public String toString() {
			return "Bid [id=" + id + ", bidArt=" + bidArt + ", amount=" + amount + ", timestamp=" + timestamp
					+ ", bidder=" + bidder + "]";
		}
	    	    
}
