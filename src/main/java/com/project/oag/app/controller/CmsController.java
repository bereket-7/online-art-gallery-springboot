package com.project.oag.app.controller;

import com.project.oag.app.dto.ContactRequestDto;
import com.project.oag.app.service.CmsService;
import com.project.oag.common.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1")
@Tag(name = "CMS")
public class CmsController {

    private final CmsService cmsService;

    public CmsController(CmsService cmsService) {
        this.cmsService = cmsService;
    }

    @GetMapping("/cms/config")
    public ResponseEntity<GenericResponse> getConfig() {
        return prepareResponse(HttpStatus.OK, "CMS config", cmsService.getConfig());
    }

    @PutMapping("/cms/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> updateConfig(@RequestBody Map<String, String> body) {
        return prepareResponse(HttpStatus.OK, "CMS config updated", cmsService.updateConfig(body));
    }

    @PostMapping("/contact")
    public ResponseEntity<GenericResponse> contact(@Valid @RequestBody ContactRequestDto dto) {
        cmsService.saveContact(dto.getName(), dto.getEmail(), dto.getMessage());
        return prepareResponse(HttpStatus.OK, "Message received", null);
    }
}
