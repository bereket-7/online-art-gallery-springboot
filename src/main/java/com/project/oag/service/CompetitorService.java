package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.controller.dto.CompetitorDto;
import com.project.oag.entity.Competitor;

public interface CompetitorService {

	List<Competitor> getAllCompetitors();

	Optional<Competitor> getCompetitorById(Long id);

	void registerCompetitor(Competitor competitor);

	void deleteCompetitor(Long id);

	Competitor updateCompetitor(Long id, Competitor competitor) throws Exception;

	boolean hasUserVoted(Long id, String ipAddress);

	void incrementVoteCount(Long id);

	void recordUserVote(Long id, String ipAddress);

	List<CompetitorDto> getTopCompetitors();

}
