package com.project.oag.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
	
	@Column(name = "expiry_date", nullable = false, columnDefinition = "DATE")
    private LocalDate expiryDate;
	
	@OneToMany(mappedBy = "competition")
    private List<Competitor> competitor;

	public Competition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Competition(String competitionTitle, String competitionDescription, int numberOfCompetitor,
			LocalDate expiryDate, List<Competitor> competitor) {
		super();
		this.competitionTitle = competitionTitle;
		this.competitionDescription = competitionDescription;
		this.numberOfCompetitor = numberOfCompetitor;
		this.expiryDate = expiryDate;
		this.competitor = competitor;
	}

	public Competition(String competitionTitle, String competitionDescription, int numberOfCompetitor,
			LocalDate expiryDate) {
		super();
		this.competitionTitle = competitionTitle;
		this.competitionDescription = competitionDescription;
		this.numberOfCompetitor = numberOfCompetitor;
		this.expiryDate = expiryDate;
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

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
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
