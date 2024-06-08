package com.project.oag.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    @JsonProperty("sendTo")
    private String sendTo;
    @JsonProperty("sendTo")
    private String subject;
    @JsonProperty("sendTo")
    private String body;
}
