package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/admin/artwork")
public class AdminArtworkController {

    private final ArtworkService artworkService;

    public AdminArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getAllArtworks() {
        return prepareResponse(HttpStatus.OK, "Artworks retrieved", artworkService.getAllArtworks());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getArtworkById(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Artwork retrieved", artworkService.getArtworkById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> updateArtwork(@PathVariable Long id,
                                                         @RequestBody ArtworkRequestDto artworkRequestDto) {
        return prepareResponse(HttpStatus.OK, "Artwork updated", artworkService.updateArtwork(id, artworkRequestDto));
    }

    @GetMapping("/category")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getArtworksByCategory(@RequestParam(required = false) String artworkCategory) {
        return prepareResponse(HttpStatus.OK, "Artworks retrieved", artworkService.getArtworkByCategory(artworkCategory));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_ARTWORK')")
    public ResponseEntity<GenericResponse> deleteArtwork(@PathVariable Long id) {
        artworkService.deleteArtwork(id);
        return prepareResponse(HttpStatus.OK, "Artwork deleted", null);
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getArtworksByStatus(@RequestParam(required = false) ArtworkStatus status) {
        return prepareResponse(HttpStatus.OK, "Artworks retrieved", artworkService.getArtworkByStatus(status));
    }

    @PatchMapping("/change/status/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> changeStatus(@PathVariable Long id,
                                                        @RequestParam ArtworkStatus status) {
        return prepareResponse(HttpStatus.OK, "Artwork status updated", artworkService.changeArtworkStatus(id, status));
    }
}
