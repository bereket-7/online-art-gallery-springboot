package com.project.oag.app.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtworkRequestDto {
    List<MultipartFile> imageFiles;
    private String artworkName;
    private String artworkDescription;
    private String artworkCategory;
    private BigDecimal price;
    private String size;
    private Integer quantity;
    private String medium;
    private Integer yearCreated;
    private String dimensions;
    private String framing;
    private Integer editionNumber;
    private Integer editionSize;
    private String rejectionReason;
}
