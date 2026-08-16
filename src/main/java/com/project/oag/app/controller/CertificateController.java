package com.project.oag.app.controller;

import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.service.CoaService;
import com.project.oag.common.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/certificates")
@Tag(name = "Certificates")
public class CertificateController {

    private final CoaService coaService;

    public CertificateController(CoaService coaService) {
        this.coaService = coaService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse> verify(@PathVariable String code) {
        return prepareResponse(HttpStatus.OK, "Certificate retrieved",
                CommerceMappers.toCertificateDto(coaService.verifyByCode(code)));
    }
}
