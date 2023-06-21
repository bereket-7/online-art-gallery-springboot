package com.project.oag.competition;

import java.util.List;
import java.util.Optional;

public interface CompetitionService {
	Competition getCompetitionById(Long competitionId);
	
	void deleteCompetition(Long id);

	List<Competition> getAllCompetitions();

	void updateCompetition(Long id, Competition comp);

	void deleteCompeition(Long id);

	void addCompetition(Competition comp);

	Competition getMostRecentCompetition();

	Integer getNumberOfCompetitor(Long id);


}
