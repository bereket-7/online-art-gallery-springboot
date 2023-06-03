package com.project.oag.bidding;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.project.oag.entity.User;
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
	    @JoinColumn(name = "artwork_id")
	    private BidArt artwork;
	    @Column(name = "amount")
	    private BigDecimal amount;
	    @Column(nullable = false)
	    private LocalDateTime timestamp;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", referencedColumnName = "id")
	    private User user;

		public Bid() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public BidArt getArtwork() {
			return artwork;
		}
		public void setArtwork(BidArt artwork) {
			this.artwork = artwork;
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
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
}
