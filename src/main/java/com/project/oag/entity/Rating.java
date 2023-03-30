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
	 
	 @Column(name = "stars", nullable = false)
	 private int stars;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "artwork_id", nullable = false)
	    private Artwork artwork;

	   /* @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;*/

		public Rating() {
	    	
	    }

		public Rating(int stars, Artwork artwork) {
			super();
			this.stars = stars;
			this.artwork = artwork;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getStars() {
			return stars;
		}

		public void setStars(int stars) {
			this.stars = stars;
		}

		public Artwork getArtwork() {
			return artwork;
		}

	    public void setArtwork(Artwork artwork) {
			this.artwork = artwork;
		}	    
}
