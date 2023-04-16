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
		competitor.setFirstName(competitor.getFirstName());
		competitor.setLastName(competitor.getLastName());
		competitor.setPhone(competitor.getPhone());
		competitor.setEmail(competitor.getEmail());
		competitor.setCategory(competitor.getCategory());
		competitor.setArtDescription(competitor.getArtDescription());
		competitor.setArtworkPhoto(competitor.getArtworkPhoto());
		competitorRepository.save(competitor);
	}

	@Override
	public void deleteCompetitor(Long id) {
		competitorRepository.deleteById(id);
		
	}
	
	@Override
    public Competitor updateCompetitor(Long id, Competitor competitorUpdate) throws Exception {
        return competitorRepository.findById(id).map(competitor -> {
                    competitor.setFirstName(competitorUpdate.getFirstName());
                    competitor.setLastName(competitorUpdate.getLastName());
                    competitor.setPhone(competitorUpdate.getPhone());
                    competitor.setArtDescription(competitorUpdate.getArtDescription());
                    competitor.setArtworkPhoto(competitorUpdate.getArtworkPhoto());
                    competitor.setCategory(competitorUpdate.getCategory());
                    return competitorRepository.save(competitor);
                }).orElseThrow(() -> new Exception("Competitor with this id not found"));
    }
		
}
