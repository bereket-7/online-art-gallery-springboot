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
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id")
    private Artwork artwork;

    @Column(nullable = false)
    private int rating;

    @Column(unique = true, nullable = false)
    private String userIdAndArtworkId;

    public Rating() {
        this.userIdAndArtworkId = "";
    }

    public Rating(User user, Artwork artwork, int rating) {
        this.user = user;
        this.artwork = artwork;
        this.rating = rating;
        this.userIdAndArtworkId = user.getId() + ":" + artwork.getId();
    }
}
