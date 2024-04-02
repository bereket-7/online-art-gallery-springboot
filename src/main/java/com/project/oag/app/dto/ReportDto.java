package com.project.oag.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReportDto {
    @JsonProperty("reportDetail")
    private String reportDetail;

    @JsonProperty("reportTitle")
    private String reportTitle;

    @JsonProperty("reportName")
    private String reporterName;

    @JsonProperty("reporterEmail")
    private String reporterEmail;

}
