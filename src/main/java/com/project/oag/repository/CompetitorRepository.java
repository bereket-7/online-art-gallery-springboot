package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Competitor;

public interface CompetitorRepository extends JpaRepository<Competitor,Long> {

	List<Competitor> findTop10ByOrderByVoteDesc();

}
