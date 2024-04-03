package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Entity
	@Table(name = "vote")
	public class Vote {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "competitor_id", nullable = false)
		@JsonIgnore
		//@JsonIgnoreProperties({"notifications","artworks"})
		private Competitor competitor;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "competition_id")
		//@JsonIgnoreProperties({"notifications","artworks"})
		private Competition competition;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "user_id", nullable = false)
		private User user;

	}


