package com.project.oag.entity;

import java.math.BigDecimal;

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
	    
	    private BigDecimal amount;

		public Bid() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Bid(BidArt bidArt, BigDecimal amount) {
			super();
			this.bidArt = bidArt;
			this.amount = amount;
		}

		public Bid(boolean b, String message) {
			// TODO Auto-generated constructor stub
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
	    
}
