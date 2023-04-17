package com.project.oag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Rating {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 
	 @Column(name = "values", nullable = true)
	 private int rating;

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "artwork_id", nullable = true)
	 private Artwork artwork;
	    
	 @ManyToOne(fetch = FetchType.LAZY)
	 private User user;

		public Rating() {
	    	
	    }
		public Rating(int rating, Artwork artwork, User user) {
			super();
			this.rating = rating;
			this.artwork = artwork;
			this.user = user;
		}


		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}

		public Artwork getArtwork() {
			return artwork;
		}

		public void setArtwork(Artwork artwork) {
			this.artwork = artwork;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
}
