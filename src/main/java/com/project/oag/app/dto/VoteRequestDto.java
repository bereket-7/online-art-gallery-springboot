package com.project.oag.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequestDto {
    @NotNull
    private Long competitionId;
    @NotNull
    private Long competitorId;
}
