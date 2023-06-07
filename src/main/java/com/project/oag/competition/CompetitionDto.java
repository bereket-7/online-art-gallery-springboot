package com.project.oag.competition;

import java.time.LocalDate;

public class CompetitionDto {

	private String competitionTitle;
	private String competitionDescription;
	private int numberOfCompetitor;
	private LocalDate expiryDate;
	public CompetitionDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CompetitionDto(String competitionTitle, String competitionDescription, int numberOfCompetitor,
			LocalDate expiryDate) {
		super();
		this.competitionTitle = competitionTitle;
		this.competitionDescription = competitionDescription;
		this.numberOfCompetitor = numberOfCompetitor;
		this.expiryDate = expiryDate;
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
}
