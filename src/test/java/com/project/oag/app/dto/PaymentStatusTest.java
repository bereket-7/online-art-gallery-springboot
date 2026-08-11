package com.project.oag.app.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentStatusTest {

    @Test
    void failedStatusExists() {
        assertNotNull(PaymentStatus.valueOf("FAILED"));
        assertNotNull(PaymentStatus.valueOf("INITIALIZED"));
    }
}
