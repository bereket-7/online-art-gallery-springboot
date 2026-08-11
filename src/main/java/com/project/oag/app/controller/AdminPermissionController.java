package com.project.oag.app.controller;

import com.project.oag.app.service.PermissionService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("/api/v1/admin/permissions")
public class AdminPermissionController {

    private final PermissionService permissionService;

    public AdminPermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_PERMISSIONS')")
    public ResponseEntity<GenericResponse> listPermissions() {
        return prepareResponse(HttpStatus.OK, "Permissions fetched", permissionService.findAll());
    }

    @PutMapping("/roles/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_PERMISSIONS')")
    public ResponseEntity<GenericResponse> assignPermissions(@PathVariable Long roleId,
                                                             @RequestBody List<Long> permissionIds) {
        return prepareResponse(HttpStatus.OK, "Permissions assigned",
                permissionService.assignPermissionsToRole(roleId, permissionIds));
    }
}
