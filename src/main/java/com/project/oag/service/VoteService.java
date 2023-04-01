package com.project.oag.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Competitor;
import com.project.oag.entity.User;
import com.project.oag.entity.Vote;
import com.project.oag.repository.VoteRepository;

@Service
public class VoteService {
	//private final VoteRepository voteRepository;
	@Autowired
	private final VoteRepository voteRepository;

	public VoteService(VoteRepository voteRepository) {
		super();
		this.voteRepository = voteRepository;
	}


	public Vote save(Vote vote) {
		return voteRepository.save(vote);
	}
	
    public boolean vote(User user, Competitor competitor) {
        Optional<Vote> existingVote = voteRepository.findByUserIdAndCompetitorId(user.getId(), competitor.getId());

        if (existingVote.isPresent()) {
            // user has already voted for this artwork
            return false;
        }

        Vote vote = new Vote(user, competitor);
        voteRepository.save(vote);
        return true;
    }
}
