package com.project.oag.report;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.report.Report;
import com.project.oag.report.ReportRepository;

@Service
public class ReportService {

    @Autowired
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report createReport(final Report report) {
        return this.reportRepository.saveAndFlush(report);
    }

    public Optional<Report> getReportById(final Long id) {
        return this.reportRepository.findById(id);
    }

    public List<Report> getAllReports() {
        return this.reportRepository.findAll();
    }

    public void deleteReportById(final Long id) {
        this.reportRepository.deleteById(id);
    }

}
