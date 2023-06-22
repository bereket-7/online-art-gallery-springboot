package com.project.oag.competition;

import java.util.*;

import com.project.oag.exceptions.CompetitionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CompetitorService{
	@Autowired 
	private CompetitorRepository competitorRepository;
	@Autowired 
	private VoteRepository voteRepository;
	public void saveCompetitor(Competitor competitor) {
		competitorRepository.save(competitor);
	}
	public List<Competitor> getAllCompetitors() {
		return competitorRepository.findAll();
	}
	public Competitor getCompetitorById(Long competitorId) {
		return competitorRepository.findById(competitorId)
				.orElseThrow(() -> new CompetitionNotFoundException("Competitor not found"));
	}
	public void deleteCompetitor(Long id) {
		competitorRepository.deleteById(id);
	}
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
	    public boolean hasUserVoted(Long competitorId, String ipAddress) {
	        return voteRepository.existsByCompetitorIdAndIpAddress(competitorId, ipAddress);
	    }
	public Competitor determineWinnerByVoteCount(Competition competition) {
		List<Competitor> competitors = competition.getCompetitor();
		if (competitors.isEmpty()) {
			return null;
		}
		Competitor winner = Collections.max(competitors, Comparator.comparingInt(Competitor::getVote));
		return winner;
	}


/*
		@Override
	    public List<Competitor> getTopCompetitors() {
	        return competitorRepository.findTop10ByOrderByVoteDesc();
	    }
		*/
}
