package com.project.oag.app.repository;

import com.project.oag.app.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("select v from Vote v where v.userId = ?1 and v.competitionId = ?2")
    boolean findByUserIdAndCompetitionId(Long userId, Long competitionId);
}
