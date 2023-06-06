package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

	boolean existsByCompetitorIdAndIpAddress(Long competitorId, String ipAddress);

}
