package com.project.oag.app.repository;

import com.project.oag.app.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserIdAndCompetitionId(Long userId, Long competitionId);
}
