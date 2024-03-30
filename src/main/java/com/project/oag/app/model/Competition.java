package com.project.oag.app.model;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name="competition")
public class Competition {
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "title")
	private String competitionTitle;
	@Column(name = "competition_description", nullable = false)
    private String competitionDescription;
	@Column(name = "competitor_number")
	private int numberOfCompetitor;
	@OneToMany(mappedBy = "competition")
    private List<Competitor> competitor;
	@Column(name = "expiry_date", nullable = false, columnDefinition = "DATE")
	private LocalDateTime expiryDate;
	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;
	@OneToOne
	@JoinColumn(name = "winner_id")
	private Competitor winner;

	@Column(name = "is_winner_announced")
	private boolean isWinnerAnnounced;

	@Column(name = "is_voting_closed")
	private boolean isVotingClosed;

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiryDate);
	}
	public Competition() {
		super();
	}
	public Competition(String competitionTitle, String competitionDescription, int numberOfCompetitor,
			LocalDateTime expiryDate, List<Competitor> competitor) {
		super();
		this.competitionTitle = competitionTitle;
		this.competitionDescription = competitionDescription;
		this.numberOfCompetitor = numberOfCompetitor;
		this.expiryDate = expiryDate;
		this.competitor = competitor;
	}
	public Competition(String competitionTitle, String competitionDescription, int numberOfCompetitor,
			LocalDateTime expiryDate) {
		super();
		this.competitionTitle = competitionTitle;
		this.competitionDescription = competitionDescription;
		this.numberOfCompetitor = numberOfCompetitor;
		this.expiryDate = expiryDate;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Competitor getWinner() {
		return winner;
	}

	public void setWinner(Competitor winner) {
		this.winner = winner;
	}

	public boolean isWinnerAnnounced() {
		return isWinnerAnnounced;
	}

	public void setWinnerAnnounced(boolean winnerAnnounced) {
		isWinnerAnnounced = winnerAnnounced;
	}

	public boolean isVotingClosed() {
		return isVotingClosed;
	}

	public void setVotingClosed(boolean votingClosed) {
		isVotingClosed = votingClosed;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompetitionTitle() {
		return competitionTitle;
	}
	public void setCompetitionTitle(String competitionTitle) {
		this.competitionTitle = competitionTitle;
	}
	public String getCompetitionDescription() {
		return competitionDescription;
	}
	public void setCompetitionDescription(String competitionDescription) {
		this.competitionDescription = competitionDescription;
	}
	public int getNumberOfCompetitor() {
		return numberOfCompetitor;
	}
	public void setNumberOfCompetitor(int numberOfCompetitor) {
		this.numberOfCompetitor = numberOfCompetitor;
	}
	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
	public List<Competitor> getCompetitor() {
		return competitor;
	}
	public void setCompetitor(List<Competitor> competitor) {
		this.competitor = competitor;
	}
	@Override
	public String toString() {
		return "Competition [id=" + id + ", competitionTitle=" + competitionTitle + ", competitionDescription="
				+ competitionDescription + ", numberOfCompetitor=" + numberOfCompetitor + ", expiryDate=" + expiryDate
				+ ", competitor=" + competitor + "]";
	}
}
