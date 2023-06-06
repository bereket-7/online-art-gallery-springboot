package com.project.oag.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Competition;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition,Long>{

}