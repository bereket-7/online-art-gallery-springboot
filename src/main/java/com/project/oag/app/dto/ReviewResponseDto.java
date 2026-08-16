package com.project.oag.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReviewResponseDto {
    private Long id;
    private Long artworkId;
    private Long userId;
    private String userName;
    private Double rating;
    private String comment;
    private Timestamp createdAt;
}
