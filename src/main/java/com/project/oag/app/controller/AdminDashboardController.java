package com.project.oag.app.controller;

import com.project.oag.app.service.AdminDashboardService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/kpis")
    @PreAuthorize("hasAuthority('ADMIN_VIEW_DASHBOARD')")
    public ResponseEntity<GenericResponse> getKpis() {
        return prepareResponse(HttpStatus.OK, "Dashboard KPIs", adminDashboardService.getKpis());
    }
}
