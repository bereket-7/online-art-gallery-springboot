package com.project.oag.app.dto;

import com.project.oag.app.entity.Rating;
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

    public ArtworkResponseDto() {
    }

    public ArtworkResponseDto(String artworkName, String artworkDescription, String artworkCategory, int price, String size, List<String> imageUrls, List<Rating> ratings, Long artistId) {
        this.artworkName = artworkName;
        this.artworkDescription = artworkDescription;
        this.artworkCategory = artworkCategory;
        this.price = price;
        this.size = size;
        this.imageUrls = imageUrls;
        this.ratings = ratings;
        this.artistId = artistId;
    }
}
