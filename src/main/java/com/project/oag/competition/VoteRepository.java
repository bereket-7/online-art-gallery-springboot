package com.project.oag.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.competition.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

	boolean existsByCompetitorIdAndIpAddress(Long competitorId, String ipAddress);
    boolean existsByUserIdAndCompetitionId(Long userId, Long competitionId);
}
