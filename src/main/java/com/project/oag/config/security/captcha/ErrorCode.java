package com.project.oag.config.security.captcha;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
    MISSING_SECRET, INVALID_SECRET,
    MISSING_RESPONSE, INVALID_RESPONSE;

    private static final Map<String, ErrorCode> errorsMap = new HashMap<>(4);

    static {
        errorsMap.put("missing-input-secret", MISSING_SECRET);
        errorsMap.put("invalid-input-secret", INVALID_SECRET);
        errorsMap.put("missing-input-response", MISSING_RESPONSE);
        errorsMap.put("invalid-input-response", INVALID_RESPONSE);
    }

    @JsonCreator
    public static ErrorCode forValue(String value) {
        return errorsMap.get(value.toLowerCase());
    }
}