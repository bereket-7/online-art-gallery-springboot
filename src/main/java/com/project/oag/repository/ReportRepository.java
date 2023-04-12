package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
