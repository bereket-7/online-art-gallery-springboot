package com.project.oag.app.controller;

import com.project.oag.app.dto.ReportDto;
import com.project.oag.app.service.ReportService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CUSTOMER_CREATE_REPORT')")
    public ResponseEntity<GenericResponse> createReport(@RequestBody ReportDto reportDto) {
        return reportService.createReport(reportDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_REPORT')")
    public ResponseEntity<GenericResponse> getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_REPORT')")
    public ResponseEntity<GenericResponse> deleteReportById(@PathVariable Long id) {
        return reportService.deleteReportById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_REPORT')")
    public ResponseEntity<GenericResponse> getAllReports() {
        return reportService.getAllReports();
    }

}
