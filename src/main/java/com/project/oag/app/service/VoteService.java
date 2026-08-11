package com.project.oag.app.service;

import com.project.oag.app.entity.Vote;
import com.project.oag.app.repository.VoteRepository;
import com.project.oag.exceptions.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public boolean hasUserVotedForCompetition(Long userId, Long competitionId) {
        return voteRepository.existsByUserIdAndCompetitionId(userId, competitionId);
    }

    @Transactional
    public Vote saveVote(Vote vote) {
        Long userId = vote.getUser().getId();
        Long competitionId = vote.getCompetition().getId();
        if (voteRepository.existsByUserIdAndCompetitionId(userId, competitionId)) {
            throw new GeneralException("You have already voted in this competition");
        }
        return voteRepository.save(vote);
    }
}
