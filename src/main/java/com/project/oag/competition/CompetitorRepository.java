package com.project.oag.competition;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor,Long> {

	List<Competitor> findTop10ByOrderByVoteDesc();

}
