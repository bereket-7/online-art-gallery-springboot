package com.project.oag.app.repository;

import com.project.oag.app.model.Competitor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor,Long> {
	@Query("select c from Competitor c where c.artistId = ?1")
	Competitor findByArtistId(Long artistId);

	@Query("SELECT c FROM Competitor c WHERE c.competitionId = ?1 ORDER BY c.voteCount DESC")
	List<Competitor> findTop10ByCompetitionIdOrderByVoteCountDesc(Long competitionId, Pageable pageable);


}
