package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.entity.Competition;

public interface CompetitionService {
	
	void deleteCompetition(Long id);

	List<Competition> getAllCompetitions();

	void updateCompetition(Long id, Competition comp);

	void deleteCompeition(Long id);

	void addCompetition(Competition comp);

	Optional<Competition> getCompetitionById(Long id);

}
