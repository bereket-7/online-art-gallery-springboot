package com.project.oag.utils;

import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {
    public static ResponseEntity<GenericResponse> prepareResponse(final HttpStatus status, final String message, final Object content) {
        return ResponseEntity.status(status)
                .body(new GenericResponse(status.value(), message, content));
    }
}
