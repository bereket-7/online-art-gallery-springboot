package com.project.oag.service;

import com.project.oag.controller.dto.CompetitionDto;
import com.project.oag.entity.Competition;

public interface CompetitionService {

	Competition createCompetiton(CompetitionDto competitionDto);
	
	void deleteCompetition(Long id);

}
