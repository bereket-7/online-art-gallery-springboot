package com.project.oag.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.CompetitorDto;
import com.project.oag.entity.Competitor;
import com.project.oag.entity.Vote;
import com.project.oag.repository.CompetitorRepository;
import com.project.oag.repository.VoteRepository;

import jakarta.transaction.Transactional;

@Service
public class CompetitorServiceImpl implements CompetitorService{
	
	@Autowired 
	CompetitorRepository competitorRepository;

	@Autowired 
	VoteRepository voteRepository; 
	
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
	
    @Override
    @Transactional
    public void incrementVoteCount(Long id) {
        Competitor competitor = competitorRepository.findById(id).get();
        competitor.setVote(competitor.getVote() + 1);
        competitorRepository.save(competitor);
    }
	
		@Override
	    public boolean hasUserVoted(Long competitorId, String ipAddress) {
	        return voteRepository.existsByCompetitorIdAndIpAddress(competitorId, ipAddress);
	    }
		@Override
	    public void recordUserVote(Long competitorId, String ipAddress) {
	        Vote vote = new Vote();
	        vote.setCompetitorId(competitorId);
	        vote.setIpAddress(ipAddress);

	        voteRepository.save(vote);
	    }
/*
		@Override
	    public List<Competitor> getTopCompetitors() {
	        return competitorRepository.findTop10ByOrderByVoteDesc();
	    }
		*/
	
		@Override
		 public List<CompetitorDto> getTopCompetitors() {
		        List<Competitor> competitors = competitorRepository.findAll();
		        List<Vote> votes = voteRepository.findAll();
		        
		        // Map competitors to their vote count
		        Map<Long, Integer> voteCounts = new HashMap<>();
		        for (Vote vote : votes) {
		            Long competitorId = vote.getCompetitorId();
		            voteCounts.put(competitorId, voteCounts.getOrDefault(competitorId, 0) + 1);
		        }
		        
		        // Sort competitors by vote count
		        competitors.sort((c1, c2) -> voteCounts.getOrDefault(c2.getId(), 0) - voteCounts.getOrDefault(c1.getId(), 0));
		        
		        // Create a CompetitorDto list with only firstName and artworkPhoto
		        List<CompetitorDto> dtos = new ArrayList<>();
		        for (Competitor competitor : competitors) {
		            CompetitorDto dto = new CompetitorDto();
		            dto.setFirstName(competitor.getFirstName());
		            dto.setArtworkPhoto(competitor.getArtworkPhoto());
		            dtos.add(dto);
		        }
		        
		        // Return only the first element of the list
		        return dtos.subList(0, 1);
		    }
}
