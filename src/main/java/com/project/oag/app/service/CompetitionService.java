package com.project.oag.app.service;

import com.project.oag.app.dto.CompetitionDto;
import com.project.oag.app.entity.Competition;
import com.project.oag.app.repository.CompetitionRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CompetitionService {
    private final ModelMapper modelMapper;
    private final CompetitionRepository competitionRepository;

    public CompetitionService(ModelMapper modelMapper, CompetitionRepository competitionRepository) {
        this.modelMapper = modelMapper;
        this.competitionRepository = competitionRepository;
    }

    public Competition getCompetitionById(Long competitionId) {
        return competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));
    }

    @Transactional
    public void deleteCompetition(Long id) {
        competitionRepository.deleteById(id);
    }

    public List<Competition> getAllCompetitions() {
        return competitionRepository.findAll();
    }

    @Transactional
    public Competition addCompetition(CompetitionDto competitionDto) {
        val competition = modelMapper.map(competitionDto, Competition.class);
        return competitionRepository.save(competition);
    }

    @Transactional
    public Competition updateCompetition(Long id, CompetitionDto competitionDto) {
        val competition = modelMapper.map(competitionDto, Competition.class);
        competition.setId(id);
        return competitionRepository.save(competition);
    }

    public List<Competition> getExpiredCompetitions() {
        LocalDateTime currentTime = LocalDateTime.now();
        return competitionRepository.findByEndTimeBefore(currentTime);
    }

    public List<Competition> getMostRecentCompetition() {
        return competitionRepository.findAll();
    }
}


