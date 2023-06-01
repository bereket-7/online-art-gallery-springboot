package com.project.oag.registration.token;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.project.oag.entity.User;

@Getter
@Setter
@Entity

public class ConfirmationToken {

	  @SequenceGenerator(
	            name = "confirmation_token_sequence",
	            sequenceName = "confirmation_token_sequence",
	            allocationSize = 1
	    )
	    @Id
	    @GeneratedValue(
	            strategy = GenerationType.SEQUENCE,
	            generator = "confirmation_token_sequence"
	    )
	    private Long id;

	    @Column(nullable = false)
	    private String token;

	    @Column(nullable = false)
	    private LocalDateTime createdAt;

	    @Column(nullable = false)
	    private LocalDateTime expiresAt;

	    private LocalDateTime confirmedAt;
	    
	    @ManyToOne
	    @JoinColumn(
	            nullable = false,
	            name = "user_id"
	    )
	    private User user;
	    
	    public ConfirmationToken() {
	        // Default constructor
	    }
	    public ConfirmationToken(String token,
	                             LocalDateTime createdAt,
	                             LocalDateTime expiresAt,
	                             User user) {
	        this.token = token;
	        this.createdAt = createdAt;
	        this.expiresAt = expiresAt;
	        this.user = user;
	    }


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public String getToken() {
			return token;
		}


		public void setToken(String token) {
			this.token = token;
		}


		public LocalDateTime getCreatedAt() {
			return createdAt;
		}


		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}


		public LocalDateTime getExpiresAt() {
			return expiresAt;
		}


		public void setExpiresAt(LocalDateTime expiresAt) {
			this.expiresAt = expiresAt;
		}


		public LocalDateTime getConfirmedAt() {
			return confirmedAt;
		}


		public void setConfirmedAt(LocalDateTime confirmedAt) {
			this.confirmedAt = confirmedAt;
		}


		public User getUser() {
			return user;
		}


		public void setUser(User user) {
			this.user = user;
		}
}
