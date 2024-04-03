package com.project.oag.app.service;

import com.project.oag.app.model.Vote;
import com.project.oag.app.repository.VoteRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }
    public boolean hasUserVotedForCompetition(Long userId, Long competitionId) {
        return voteRepository.existsByUserIdAndCompetitionId(userId, competitionId);
    }
    public ResponseEntity<GenericResponse> saveVote(Vote vote) {
        try {
            voteRepository.save(vote);
            return prepareResponse(HttpStatus.OK, "saved successfully", null);
        } catch (Exception e) {
            throw new GeneralException("Error saving vote");
        }
    }

}
