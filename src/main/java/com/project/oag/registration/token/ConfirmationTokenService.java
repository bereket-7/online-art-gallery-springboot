package com.project.oag.registration.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.project.oag.entity.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationTokenService {
	
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

}
