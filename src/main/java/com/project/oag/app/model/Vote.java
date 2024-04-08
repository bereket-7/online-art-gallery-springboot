package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
		@Column(name = "ID")
		private Long id;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "COMPETITOR_ID")
		@JsonIgnore
		@JsonIgnoreProperties({"user","competition"})
		private Long competitorId;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "COMPETITION_ID")
		@JsonIgnoreProperties({"competitor"})
		private Long competitionId;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "USER_ID")
		private Long userId;

	}


