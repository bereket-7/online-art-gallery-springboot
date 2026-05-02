package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtistProfileDto;
import com.project.oag.app.service.ArtistProfileService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/artists")
public class ArtistProfileController {

    private final ArtistProfileService artistProfileService;

    public ArtistProfileController(ArtistProfileService artistProfileService) {
        this.artistProfileService = artistProfileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getArtistProfile(@PathVariable Long id) {
        ArtistProfileDto profile = artistProfileService.getArtistProfile(id);
        return prepareResponse(HttpStatus.OK, "Artist Profile retrieved", profile);
    }
}
