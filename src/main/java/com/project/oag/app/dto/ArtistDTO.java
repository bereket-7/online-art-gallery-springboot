package com.project.oag.app.dto;

import com.project.oag.app.entity.Artwork;
import lombok.Data;

import java.util.List;

@Data
public class ArtistDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private List<Artwork> artworks;
}
