package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.CompetitorRequestDto;
import com.project.oag.app.model.Competitor;
import com.project.oag.app.model.User;
import com.project.oag.app.model.Vote;
import com.project.oag.app.repository.CompetitionRepository;
import com.project.oag.app.repository.CompetitorRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.repository.VoteRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class CompetitorService{
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

    public ResponseEntity<GenericResponse> registerCompetitor(HttpServletRequest request,CompetitorRequestDto competitorRequestDto) {
		Long userId = getUserId(request);
		try {
			val competitor = modelMapper.map(competitorRequestDto, Competitor.class);
			competitor.setArtistId(userId);
			val response = competitorRepository.save(competitor);
			return prepareResponse(HttpStatus.OK,"Registered successfully",response);
		} catch (Exception e) {
			throw new GeneralException(" Failed to save competitor");
		}
	}
	public ResponseEntity<GenericResponse> getAllCompetitors() {
		try {
			val competitors = competitorRepository.findAll();
			List<ArtworkResponseDto> response = competitors.stream().map((element) -> modelMapper.map(element, ArtworkResponseDto.class))
					.collect(Collectors.toList());
			return prepareResponse(HttpStatus.OK,"Available competitors",response);
		} catch (Exception e) {
			throw new GeneralException("Failed to get competitors");
		}
	}
	public ResponseEntity<GenericResponse> deleteCompetitor(final Long id) {
		try {
			competitorRepository.deleteById(id);
			return prepareResponse(HttpStatus.OK, "Successfully deleted", null);
		} catch (Exception e) {
			throw new GeneralException("Failed to delete competitor");
		}
	}
	public ResponseEntity<GenericResponse> updateCompetitor(HttpServletRequest request, CompetitorRequestDto competitorRequestDto) {
		Long userId = getUserId(request);
		val competitor = competitorRepository.findByArtistId(userId);
		try {
			modelMapper.map(competitorRequestDto, competitor);
			val response = competitorRepository.save(competitor);
			log.info(LOG_PREFIX, "Saved Competitor information", "");
			return prepareResponse(HttpStatus.OK, "Competitor Updated successfully ", response);
		}catch (Exception e){
			throw new GeneralException("Failed to update competitor");
		}
	}

	public ResponseEntity<GenericResponse> getCompetitorById(Long id) {
		try {
			val response = competitorRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Competitor not found"));
			return prepareResponse(HttpStatus.OK,"Successfully retrieved competitor",response);
		} catch (Exception e) {
			throw new GeneralException("Failed to get competitor ");
		}
	}

	public ResponseEntity<GenericResponse> voteForCompetitor(Long competitionId,
															 Long competitorId,
															 HttpServletRequest request){
		Long userId = getUserId(request);
		try {
			competitionRepository.findById(competitionId)
					.orElseThrow(() -> new ResourceNotFoundException("Competition not found"));

			if ((voteService.isUserVotedForCompetition(userId, competitionId))) {
				Vote vote = new Vote();
				vote.setCompetitorId(competitorId);
				vote.setUserId(userId);
				vote.setCompetitionId(competitionId);
				voteService.saveVote(vote);
				Competitor competitor = new Competitor();
				int currentVote = competitor.getVoteCount();
				competitor.setVoteCount(currentVote++);
				competitorRepository.save(competitor);
			}
			return prepareResponse(HttpStatus.OK, "Thanks for voting", null);
		} catch (ResourceNotFoundException e) {
			throw new GeneralException("failed to vote for this competitor");
		}
	}

	public ResponseEntity<GenericResponse> getWinner(Long competitionId) {
		try {
            competitionRepository.findById(competitionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));
            List<Competitor> response = competitorRepository.findTop10ByCompetitionIdOrderByVoteCountDesc(competitionId, PageRequest.of(0, 10));
            return prepareResponse(HttpStatus.OK, "Top 10 winners in this competition", response);
        } catch (ResourceNotFoundException e) {
            throw new GeneralException("Failed to get winner");
        }
	}
	private Long getUserId(HttpServletRequest request) {
		return getUserByUsername(getLoggedInUserName(request)).getId();
	}

	private User getUserByUsername(String email) {
		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
	}

}
