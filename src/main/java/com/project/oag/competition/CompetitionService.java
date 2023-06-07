package com.project.oag.competition;

import java.util.List;
import java.util.Optional;

public interface CompetitionService {
	
	void deleteCompetition(Long id);

	List<Competition> getAllCompetitions();

	void updateCompetition(Long id, Competition comp);

	void deleteCompeition(Long id);

	void addCompetition(Competition comp);

	Optional<Competition> getCompetitionById(Long id);

	Competition getMostRecentCompetition();

	Integer getNumberOfCompetitor(Long id);


}
