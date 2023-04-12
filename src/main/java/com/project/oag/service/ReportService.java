package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Report;
import com.project.oag.repository.ReportRepository;

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

    /*
     * public Report updateReportById(final Long id, final Report updatedData) {
     * final Optional<Report> existingData = Optional.ofNullable(this.getOne(id));
     * existingData.ifPresent(data -> {
     * data.setReportTitle(updatedData.getReportTitle());
     * data.setReportDetail(updatedData.getReportDetail());
     * data.setReporterName(updatedData.getReporterName());
     * data.setReporterEmail(updatedData.getReporterEmail());
     * });
     * existingData.orElseThrow(() -> new
     * ResourceNotFoundException("No Data Found with Id : " + id));
     * return this.saveAndFlush(existingData);
     * }
     */

}
