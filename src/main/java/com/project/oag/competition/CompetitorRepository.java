package com.project.oag.competition;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Competitor;
@Repository
public interface CompetitorRepository extends JpaRepository<Competitor,Long> {

	List<Competitor> findTop10ByOrderByVoteDesc();

}
