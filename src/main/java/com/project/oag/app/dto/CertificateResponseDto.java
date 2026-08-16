package com.project.oag.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CertificateResponseDto {
    private Long id;
    private Long orderItemId;
    private Long artworkId;
    private String verificationCode;
    private Timestamp issuedAt;
}
