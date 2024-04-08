package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="COMPETITION")
public class Competition {
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TITLE")
	private String competitionTitle;

	@Column(name = "COMPETITION_DESCRIPTION")
    private String competitionDescription;

	@Column(name = "COMPETITOR_NUMBER")
	private int numberOfCompetitor;

	@JsonIgnore
	@OneToMany(mappedBy = "competition")
    private List<Competitor> competitor;

	@Column(name = "EXPIRY_DATE")
	private LocalDateTime expiryDate;

	@Column(name = "END_TIME")
	private LocalDateTime endTime;

	@Column(name = "IS_WINNER_ANNOUNCED")
	private boolean isWinnerAnnounced;

	@Column(name = "IS_VOTING_CLOSED")
	private boolean isVotingClosed;
}
