package com.project.oag.competition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CompetitorServiceImpl implements CompetitorService{
	
	@Autowired 
	CompetitorRepository competitorRepository;

	@Autowired 
	VoteRepository voteRepository; 
	
	@Override
	public void saveCompetitor(Competitor competitor) {
		competitorRepository.save(competitor);
	}
	
	@Override
	public List<Competitor> getAllCompetitors() {
		return competitorRepository.findAll();
	}

	@Override
	public Optional<Competitor> getCompetitorById(Long id) {
		return competitorRepository.findById(id);
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
                    competitor.setImage(competitorUpdate.getImage());
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
		        Map<Long, Integer> voteCounts = new HashMap<>();
		        for (Vote vote : votes) {
					Long competitorId = vote.getCompetitorId();
					voteCounts.put(competitorId, voteCounts.getOrDefault(competitorId, 0) + 1);
				}
		        competitors.sort((c1, c2) -> voteCounts.getOrDefault(c2.getId(), 0) - voteCounts.getOrDefault(c1.getId(), 0));
		        List<CompetitorDto> dtos = new ArrayList<>();
		        for (Competitor competitor : competitors) {
		            CompetitorDto dto = new CompetitorDto();
		            dto.setFirstName(competitor.getFirstName());
		            dto.setImage(competitor.getImage());
		            dtos.add(dto);
		        }
		        return dtos.subList(0, 1);
		    }
}
