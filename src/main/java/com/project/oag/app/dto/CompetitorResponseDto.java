package com.project.oag.app.dto;

import lombok.Data;

@Data
public class CompetitorResponseDto {
    private Long artistId;
    private Long competitionId;
    private int vote;
    private String imageUrl;
    private String artDescription;
    private String category;
}
