package com.project.oag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote,Long>{
	 Optional<Vote> findByUserIdAndCompetitorId(Long userId, Long competitorId);
	 
}
