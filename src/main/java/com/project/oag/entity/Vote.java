package com.project.oag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
 

@Entity
@IdClass(VoteId.class)
public class Vote {
	@Id
	private Long userId;
    @Id
    private Long competitorId;
	
	@Column(name="vote")
	private int numberOfVote;
	
    @ManyToOne
	@MapsId("competitorId")
	private Competitor competitor;
	
	 @ManyToOne
	 @MapsId("userId")
	 private User user;
	
	public Vote() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    public Vote(User user, Competitor competitor) {
        this.userId = user.getId();
        this.competitorId = competitor.getId();
        this.user = user;
        this.competitor = competitor;
    }

	public Vote(Long userId, Long competitorId, int numberOfVote, Competitor competitor, User user) {
		super();
		this.userId = userId;
		this.competitorId = competitorId;
		this.numberOfVote = numberOfVote;
		this.competitor = competitor;
		this.user = user;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCompetitorId() {
		return competitorId;
	}

	public void setCompetitorId(Long competitorId) {
		this.competitorId = competitorId;
	}

	public int getNumberOfVote() {
		return numberOfVote;
	}

	public void setNumberOfVote(int numberOfVote) {
		this.numberOfVote = numberOfVote;
	}

	public Competitor getCompetitor() {
		return competitor;
	}

	public void setCompetitor(Competitor competitor) {
		this.competitor = competitor;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
