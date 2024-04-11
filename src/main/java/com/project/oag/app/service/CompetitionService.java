package com.project.oag.app.service;

import com.project.oag.app.dto.CompetitionDto;
import com.project.oag.app.model.Competition;
import com.project.oag.app.model.Competitor;
import com.project.oag.app.repository.CompetitionRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class CompetitionService {
	private final ModelMapper modelMapper;
    private final CompetitionRepository competitionRepository;
    public CompetitionService(ModelMapper modelMapper, CompetitionRepository competitionRepository) {
        this.modelMapper = modelMapper;
        this.competitionRepository = competitionRepository;
    }

    public ResponseEntity<GenericResponse> getCompetitionById(Long competitionId) {
		try {
			val response = competitionRepository.findById(competitionId)
					.orElseThrow(() -> new ResourceNotFoundException("Competition not found"));
			return prepareResponse(HttpStatus.OK, "Successfully retrieved competition", response);
		} catch (Exception e) {
			throw new GeneralException(" Could not find competition");
		}
	}
	public ResponseEntity<GenericResponse> deleteCompetition(Long id) {
		try {
			competitionRepository.deleteById(id);
			return prepareResponse(HttpStatus.OK, "Successfully deleted competition", null);
		} catch (Exception e) {
			throw new GeneralException("Error deleting");
		}
	}
    public ResponseEntity<GenericResponse> getAllCompetitions() {
		try {
			val response = competitionRepository.findAll();
			return prepareResponse(HttpStatus.OK, "Competitions ", response);
		} catch (Exception e) {
			throw new GeneralException("failed to get all competitions");
		}
	}
	public ResponseEntity<GenericResponse> addCompetition (CompetitionDto competitionDto){
		try {
			val competition = modelMapper.map(competitionDto,Competition.class);
			val response = competitionRepository.save(competition);
			return prepareResponse(HttpStatus.OK, "Successfully added competition", response);
		} catch (Exception e) {
			throw new GeneralException("Failed to add competition");
		}
	}
	public ResponseEntity<GenericResponse> updateCompetition (Long id, CompetitionDto competitionDto){
		try {
			val competition = modelMapper.map(competitionDto,Competition.class);
			val response = competitionRepository.save(competition);
			return prepareResponse(HttpStatus.OK, "Successfully updated competition", response);
		} catch (Exception e) {
			throw new GeneralException("Failed to update competition");
		}
	}
	public ResponseEntity<GenericResponse> getExpiredCompetitions() {
		LocalDateTime currentTime = LocalDateTime.now();
		try {
			val response = competitionRepository.findByEndTimeBefore(currentTime);
			return prepareResponse(HttpStatus.OK, "Fetched competition", response);
		}
		catch (Exception e) {
			throw new GeneralException("Failed to fetch competition");
		}
	}

	public ResponseEntity<GenericResponse> deleteCompetitiontById(final Long id) {
		try {
			val response = competitionRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Competition record not found"));
			competitionRepository.deleteById(id);
			return prepareResponse(HttpStatus.OK,"Successfully deleted competition",response);
		} catch (Exception e) {
			throw new GeneralException(" Failed to delete competition " + id);
		}
	}
    public ResponseEntity<GenericResponse> getMostRecentCompetition() {
		try {
			List<Competition> competitions = competitionRepository.findAll();
			return prepareResponse(HttpStatus.OK,"Most recent competitions",competitions);
		} catch (Exception e) {
			throw new GeneralException("failed to retrieve competition");
		}
    }
	public Competitor determineWinner(Long competitionId) {
		Competition competition = competitionRepository.findById(competitionId).orElse(null);
		if (competition == null || competition.isWinnerAnnounced()) {
			return null;
		}
		List<Competitor> competitors = competition.getCompetitor();
		if (competitors.isEmpty()) {
			return null;
		}
		Competitor winner = Collections.max(competitors, Comparator.comparingInt(Competitor::getVote));
		return winner;
	}
}



