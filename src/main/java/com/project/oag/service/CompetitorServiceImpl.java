package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Competitor;
import com.project.oag.repository.CompetitorRepository;

@Service
public class CompetitorServiceImpl implements CompetitorService{
	
	@Autowired 
	CompetitorRepository competitorRepository;

	@Override
	public List<Competitor> getAllCompetitors() {
		return competitorRepository.findAll();
	}

	@Override
	public Optional<Competitor> getCompetitorById(Long id) {
		return competitorRepository.findById(id);
	}

	@Override
	public void registerCompetitor(Competitor competitor) {
	  competitorRepository.save(competitor);
	}
}
