package com.project.oag.app.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.project.oag.app.repository.CompetitionRepository;
import com.project.oag.app.model.Competition;
import com.project.oag.app.model.Competitor;
import com.project.oag.exceptions.CompetitionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CompetitionService {
	@Autowired
    CompetitionRepository competitionRepository;
	public Competition getCompetitionById(Long competitionId) {
		return competitionRepository.findById(competitionId)
				.orElseThrow(() -> new CompetitionNotFoundException("Competition not found"));
	}
	public void deleteCompetition(Long id) {
		competitionRepository.deleteById(id);
	}
    public List<Competition> getAllCompetitions() {
		return competitionRepository.findAll();
    }
	public void addCompetition (Competition comp){
	    	competitionRepository.saveAndFlush(comp);
	    	}
	public void updateCompetition (Long id, Competition comp){
	    	competitionRepository.saveAndFlush(comp);
}
	public List<Competition> getExpiredCompetitions() {
		LocalDateTime currentTime = LocalDateTime.now();
		return competitionRepository.findByEndTimeBefore(currentTime);
	}
	public void deleteCompeition (Long id){
	    	 competitionRepository.deleteById(id);
	}

    public Competition getMostRecentCompetition() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("expiryDate").descending());
        List<Competition> competitions = competitionRepository.findAll(pageable).getContent();
        if (!competitions.isEmpty()) {
            return competitions.get(0);
        }
        return null;
    }
	public Integer getNumberOfCompetitor(Long id) {
        Optional<Competition> optionalCompetition = competitionRepository.findById(id);
        if (optionalCompetition.isPresent()) {
            return optionalCompetition.get().getNumberOfCompetitor();
        } else {
            return null;
        }
    }
	public Competitor determineWinner(Long competitionId) {
		Competition competition = competitionRepository.findById(competitionId).orElse(null);
		if (competition == null || competition.isWinnerAnnounced()) {
			return null;
		}
		List<Competitor> competitors = competition.getCompetitor();
		if (competitors.isEmpty()) {
			return null;
		}
		Competitor winner = Collections.max(competitors, Comparator.comparingInt(Competitor::getVote));
		return winner;
	}
}



