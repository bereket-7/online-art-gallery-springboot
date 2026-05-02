package com.project.oag.app.dto;

import lombok.Data;
import java.util.List;

@Data
public class ArtistProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
    
    // Summary of artworks for public viewing
    private List<ArtworkSummaryDto> artworks;
    
    @Data
    public static class ArtworkSummaryDto {
        private Long id;
        private String title;
        private String imageUrl;
        private String category;
        private Double price;
        private boolean isAvailable;
    }
}
