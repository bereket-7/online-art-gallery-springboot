package com.project.oag.app.dto;

import com.project.oag.app.model.Rating;
import lombok.Data;

import java.util.List;

@Data
public class ArtworkResponseDto {
    private String artworkName;
    private String artworkDescription;
    private String artworkCategory;
    private int price;
    private String size;
    private List<String> imageUrls;
    private List<Rating> ratings;
    private Long artistId;
}
