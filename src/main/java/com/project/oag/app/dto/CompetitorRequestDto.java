package com.project.oag.app.dto;

import lombok.Data;

@Data
public class CompetitorRequestDto {
    private Long artistId;
    private Long competitionId;
    private String imageUrl;
    private String artDescription;
    private String category;
}
