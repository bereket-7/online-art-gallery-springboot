package com.project.oag.app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompetitionDto {
	private String competitionTitle;

	private String competitionDescription;

	private int numberOfCompetitor;

	private LocalDateTime expiryDate;

	private LocalDateTime endTime;
}
