package com.project.oag.app.service;

import com.project.oag.app.dto.CompetitorRequestDto;
import com.project.oag.app.model.Competition;
import com.project.oag.app.model.Competitor;
import com.project.oag.app.model.Event;
import com.project.oag.app.repository.CompetitorRepository;
import com.project.oag.app.repository.VoteRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.CompetitionNotFoundException;
import com.project.oag.exceptions.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.project.oag.utils.Utils.prepareResponse;

@Service
public class CompetitorService{
	private final CompetitorRepository competitorRepository;
	private final VoteRepository voteRepository;
	private final ModelMapper modelMapper;

    public CompetitorService(CompetitorRepository competitorRepository, VoteRepository voteRepository, ModelMapper modelMapper) {
        this.competitorRepository = competitorRepository;
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<GenericResponse> registerCompetitor(HttpServletRequest request,CompetitorRequestDto competitorRequestDto) {
		try {
			val competitor = modelMapper.map(competitorRequestDto, Competitor.class);
			val response = competitorRepository.save(competitor);
			return prepareResponse(HttpStatus.OK,"Registered successfully",response);
		} catch (Exception e) {
			throw new GeneralException(" Failed to save competitor");
		}
	}
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
	public Competitor determineWinnerByVoteCount(Competition competition) {
		List<Competitor> competitors = competition.getCompetitor();
		if (competitors.isEmpty()) {
			return null;
		}
		Competitor winner = Collections.max(competitors, Comparator.comparingInt(Competitor::getVote));
		return winner;
	}

}
