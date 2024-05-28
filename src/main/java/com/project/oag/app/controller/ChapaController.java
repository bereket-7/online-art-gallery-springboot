package com.project.oag.app.controller;

import com.project.oag.app.service.ChapaService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
public class ChapaController {
    private final ChapaService chapaService;

    public ChapaController(ChapaService chapaService) {
        this.chapaService = chapaService;
    }

    @PostMapping("/initialize")
    public ResponseEntity<GenericResponse> pay(HttpServletRequest request) throws Throwable {
        return chapaService.pay(request);
    }

    @GetMapping("/verify/{txRef}")
    public ResponseEntity<GenericResponse> verify(@PathVariable("txRef") String txRef) throws Throwable {
        return chapaService.verify(txRef);
    }
}
