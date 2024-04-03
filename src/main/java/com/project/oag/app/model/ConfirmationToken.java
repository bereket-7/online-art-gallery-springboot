package com.project.oag.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="CONFIRMATION_TOKEN")
public class ConfirmationToken {

	  @SequenceGenerator(name = "confirmation_token_sequence",
	            sequenceName = "confirmation_token_sequence", allocationSize = 1)
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_token_sequence")
	    private Long id;

	    @Column(nullable = false)
	    private String token;

		@CreationTimestamp
		@Column(name = "CREATION_DATE")
		private Timestamp creationDate;

		@UpdateTimestamp
		@Column(name = "LAST_UPDATE_DATE")
		private Timestamp lastUpdateDate;

	    @Column(name = "CONFIRMED_AT")
	    private Timestamp confirmedAt;
	    
	    @ManyToOne
	    @JoinColumn(name = "USER_ID")
	    private User user;
}
