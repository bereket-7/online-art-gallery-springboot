package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.CompetitorRequestDto;
import com.project.oag.app.entity.Competitor;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.Vote;
import com.project.oag.app.repository.CompetitionRepository;
import com.project.oag.app.repository.CompetitorRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.repository.VoteRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class CompetitorService {
    private final CompetitorRepository competitorRepository;
    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteService voteService;
    private final ModelMapper modelMapper;

    public CompetitorService(CompetitorRepository competitorRepository, CompetitionRepository competitionRepository, UserRepository userRepository, VoteRepository voteRepository, VoteService voteService, ModelMapper modelMapper) {
        this.competitorRepository = competitorRepository;
        this.competitionRepository = competitionRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
        this.modelMapper = modelMapper;
    }

    public Competitor registerCompetitor(HttpServletRequest request, CompetitorRequestDto competitorRequestDto) {
        val competitor = modelMapper.map(competitorRequestDto, Competitor.class);
        return competitorRepository.save(competitor);
    }

    public List<ArtworkResponseDto> getAllCompetitors() {
        val competitors = competitorRepository.findAll();
        return competitors.stream().map((element) -> modelMapper.map(element, ArtworkResponseDto.class))
                .collect(Collectors.toList());
    }

    public void deleteCompetitor(final Long id) {
        competitorRepository.deleteById(id);
    }

    public Competitor updateCompetitor(HttpServletRequest request, CompetitorRequestDto competitorRequestDto) {
        Long userId = getUserId(request);
        val competitor = competitorRepository.findByArtistId(userId);
        modelMapper.map(competitorRequestDto, competitor);
        return competitorRepository.save(competitor);
    }

    public Competitor getCompetitorById(Long id) {
        return competitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competitor not found"));
    }

    public void voteForCompetitor(Long competitionId,
                                  Long competitorId,
                                  HttpServletRequest request) {
        Long userId = getUserId(request);
        competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));

        if (voteService.hasUserVotedForCompetition(userId, competitionId)) {
            throw new GeneralException("You have already voted in this competition");
        }

        Competitor competitor = competitorRepository.findById(competitorId)
                .orElseThrow(() -> new ResourceNotFoundException("Competitor not found"));

        Vote vote = new Vote();
        vote.setUser(getUserByUsername(getLoggedInUserName(request)));
        vote.setCompetition(competitor.getCompetition());
        vote.setCompetitor(competitor);
        voteService.saveVote(vote);

        competitor.setVoteCount(competitor.getVoteCount() + 1);
        competitorRepository.save(competitor);
    }

    public List<Competitor> getWinner(Long competitionId) {
        competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));
        return competitorRepository.findTop10ByCompetitionIdOrderByVoteCountDesc(competitionId, PageRequest.of(0, 10));
    }

    private Long getUserId(HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }
}
