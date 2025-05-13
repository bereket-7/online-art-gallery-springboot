package com.project.oag.utils;

import com.project.oag.app.dto.GenericResponsePageable;
import com.project.oag.app.dto.PageableDto;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class Utils {
    public static ResponseEntity<GenericResponse> prepareResponse(final HttpStatus status, final String message, final Object content) {
        return ResponseEntity.status(status)
                .body(new GenericResponse(status.value(), message, content));
    }
    public static ResponseEntity<GenericResponsePageable> prepareResponseWithPageable(final HttpStatus status, final String message, final Object content, PageableDto pageableDto) {
        return ResponseEntity.status(status)
                .body(new GenericResponsePageable<>(status.value(), message, content, pageableDto));
    }
    public static String getRandomUuid() {
        return UUID.randomUUID().toString();
    }
}
