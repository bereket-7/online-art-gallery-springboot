package com.project.oag.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StandardRequestDto {
    @JsonProperty("standardDescription")
    private String standardDescription;
    @JsonProperty("standardType")
    private StandardType standardType;
}