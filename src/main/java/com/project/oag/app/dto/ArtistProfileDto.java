package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtistProfileDto {
    private Long id;
    private String uuid;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
    private boolean verifiedArtist;
    private String slug;

    private List<ArtworkSummaryDto> artworks;

    @Data
    public static class ArtworkSummaryDto {
        private Long id;
        private String title;
        private String imageUrl;
        private String category;
        private BigDecimal price;
        private boolean available;
    }
}
