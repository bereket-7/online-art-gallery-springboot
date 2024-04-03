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
@Table(name="competition")
public class Competition {
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title")
	private String competitionTitle;

	@Column(name = "competition_description")
    private String competitionDescription;

	@Column(name = "competitor_number")
	private int numberOfCompetitor;

	@JsonIgnore
	@OneToMany(mappedBy = "competition")
    private List<Competitor> competitor;

	@Column(name = "expiry_date")
	private LocalDateTime expiryDate;

	@Column(name = "end_time")
	private LocalDateTime endTime;

	@OneToOne
	@JoinColumn(name = "winner_id")
	private Competitor winner;

	@Column(name = "is_winner_announced")
	private boolean isWinnerAnnounced;

	@Column(name = "is_voting_closed")
	private boolean isVotingClosed;
}
