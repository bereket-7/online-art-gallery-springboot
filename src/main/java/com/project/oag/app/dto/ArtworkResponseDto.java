package com.project.oag.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtworkResponseDto {
    private Long id;
    private String artworkName;
    private String artworkDescription;
    private String artworkCategory;
    private BigDecimal price;
    private String size;
    private List<String> imageUrls;
    private Long artistId;
    private String artistName;
    private String artistSlug;
    private ArtworkStatus status;
    private Integer quantity;
    private String medium;
    private Integer yearCreated;
    private String dimensions;
    private String framing;
    private Integer editionNumber;
    private Integer editionSize;
    private String rejectionReason;
    private Double averageRating;
    private Boolean allowOffers;
}
