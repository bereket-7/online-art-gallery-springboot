package com.project.oag.app.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class CollectionResponseDto {
    private Long id;
    private String slug;
    private String title;
    private String description;
    private Boolean featured;
    private Timestamp creationDate;
    private List<ArtworkResponseDto> artworks;
}
