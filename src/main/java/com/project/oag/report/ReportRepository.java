package com.project.oag.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.report.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
