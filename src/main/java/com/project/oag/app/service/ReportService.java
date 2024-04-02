package com.project.oag.app.service;

import java.awt.geom.GeneralPath;
import java.util.List;
import java.util.Optional;

import com.project.oag.app.dto.ReportDto;
import com.project.oag.app.repository.ReportRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.oag.app.model.Report;

import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final ModelMapper modelMapper;
    public ReportService(ReportRepository reportRepository, ModelMapper modelMapper) {
        this.reportRepository = reportRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<GenericResponse> createReport(ReportDto reportDto) {
        try {
            val report = modelMapper.map(reportDto, Report.class);
            reportRepository.save(report);
            return prepareResponse(HttpStatus.OK,"Successfully created report",null);
        } catch (Exception e) {
            throw new GeneralException("Failed to save report");
        }
    }

    public ResponseEntity<GenericResponse> getReportById(final Long id) {
        try {
            val response = reportRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Report record not found"));
            return prepareResponse(HttpStatus.OK,"Successfully retrieved report",response);
        } catch (Exception e) {
            throw new GeneralException("Could not find report by id " + id);
        }
    }

    public List<Report> getAllReports() {
        return this.reportRepository.findAll();
    }

    public ResponseEntity<GenericResponse> deleteReportById(final Long id) {
        try {
            val response = reportRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Report record not found"));
            reportRepository.deleteById(id);
            return prepareResponse(HttpStatus.OK,"Successfully deleted report",response);
        } catch (Exception e) {
            throw new GeneralException(" Failed to delete report " + id);
        }
    }

}
