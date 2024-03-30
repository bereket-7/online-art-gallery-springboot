package com.project.oag.app.repository;

import java.util.List;

import com.project.oag.app.model.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor,Long> {

	List<Competitor> findTop10ByOrderByVoteDesc();

}
