package com.project.oag.app.dto;

public enum PaymentStatus {
    VERIFIED,
    INITIALIZED,
    /** @deprecated use {@link #INITIALIZED}; kept for legacy DB rows */
    INTIALIZED,
    FAILED
}
