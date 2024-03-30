package com.project.oag.app.repository;

import com.project.oag.app.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition,Long>{
    List<Competition> findByEndTimeBefore(LocalDateTime currentTime);
}
