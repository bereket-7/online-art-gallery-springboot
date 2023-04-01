package com.project.oag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.CompetitionDto;
import com.project.oag.entity.Competition;
import com.project.oag.repository.CompetitionRepository;

@Service
public class CompetitionServiceImpl implements CompetitionService {
	
	@Autowired
	CompetitionRepository competitionRepository;

	@Override
	public Competition createCompetiton(CompetitionDto competitionDto) {
		Competition competition = new Competition(competitionDto.getCompetitionTitle(),competitionDto.getCompetitionDescription(),
				competitionDto.getNumberOfCompetitor(),competitionDto.getExpiryDate());
		return competitionRepository.save(competition);
	}

	@Override
	public void deleteCompetition(Long id) {
		competitionRepository.deleteById(id);
	}
}
