package com.project.oag.app.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ReportDto {
    private String reportDetail;

    private String reportTitle;

    private String reporterName;

    private String reporterEmail;

}
