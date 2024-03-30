package com.project.oag.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.app.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
