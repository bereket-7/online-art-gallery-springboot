package com.project.oag.competition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;
    public boolean hasUserVotedForCompetition(Long userId, Long competitionId) {
        return voteRepository.existsByUserIdAndCompetitionId(userId, competitionId);
    }
    public void saveVote(Vote vote) {
        voteRepository.save(vote);
    }

}
