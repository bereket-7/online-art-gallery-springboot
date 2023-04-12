package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.CompetitionDto;
import com.project.oag.entity.Competition;
import com.project.oag.repository.CompetitionRepository;

@Service
public class CompetitionServiceImpl implements CompetitionService {
	
	@Autowired
	CompetitionRepository competitionRepository;
/*
	@Override
	public Competition createCompetiton(CompetitionDto competitionDto) {
		Competition competition = new Competition(competitionDto.getCompetitionTitle(),competitionDto.getCompetitionDescription(),
				competitionDto.getNumberOfCompetitor(),competitionDto.getExpiryDate());
		return competitionRepository.save(competition);
	}*/

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
	public void updateCompetition (Long id, Competition comp)
	    {
	    	competitionRepository.saveAndFlush(comp);}

	@Override
	public void deleteCompeition (Long id){
	    	 competitionRepository.deleteById(id);
	    	 }
}



