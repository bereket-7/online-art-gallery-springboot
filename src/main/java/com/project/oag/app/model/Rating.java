package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RATING")
public class Rating {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "ID")
	 private Long id;

	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
    private User user;

	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTWORK_ID")
    private Artwork artwork;

    @Column(name = "RATING_VALUE")
    private int ratingValue;

	@CreationTimestamp
	@Column(name = "CREATION_DATE")
	private Timestamp creationDate;

	@UpdateTimestamp
	@Column(name = "LAST_UPDATE_DATE")
	private Timestamp lastUpdateDate;

}