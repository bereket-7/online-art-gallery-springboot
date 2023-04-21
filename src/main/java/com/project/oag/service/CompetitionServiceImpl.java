package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Competition;
import com.project.oag.repository.CompetitionRepository;

@Service
public class CompetitionServiceImpl implements CompetitionService {
	
	@Autowired
	CompetitionRepository competitionRepository;


	@Override
	public void deleteCompetition(Long id) {
		competitionRepository.deleteById(id);
	}
    
	@Override
    public List<Competition> getAllCompetitions() {
		return competitionRepository.findAll();
    }
	
	@Override
	public Optional<Competition> getCompetitionById(Long id) {
		  return competitionRepository.findById(id);}

	@Override
	public void addCompetition (Competition comp){
	    	competitionRepository.saveAndFlush(comp);
	    	}

	@Override
	public void updateCompetition (Long id, Competition comp){
	    	competitionRepository.saveAndFlush(comp);
}

	@Override
	public void deleteCompeition (Long id){
	    	 competitionRepository.deleteById(id);
	}
	
	@Override
    public Competition getMostRecentCompetition() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("expiryDate").descending());
        List<Competition> competitions = competitionRepository.findAll(pageable).getContent();
        if (!competitions.isEmpty()) {
            return competitions.get(0);
        }
        return null;
    }
}



