package com.project.oag.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition,Long>{
    List<Competition> findByEndTimeBefore(LocalDateTime currentTime);
}
