package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return artworkService.getAllArtworks();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getArtworkById(@PathVariable Long id) {
        return artworkService.getArtworkById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> updateArtwork(@PathVariable Long id, @RequestBody ArtworkRequestDto artworkRequestDto) {
        return artworkService.updateArtwork(id, artworkRequestDto);
    }

    @GetMapping("/category")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getArtworksByCategory(@RequestParam(required = false) String artworkCategory) {
        return artworkService.getArtworkByArtworkCategory(artworkCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_ARTWORK')")
    public ResponseEntity<GenericResponse> deleteArtwork(@PathVariable Long id) {
        return artworkService.deleteArtwork(id);
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getArtworksByStatus(@RequestParam(required = false) ArtworkStatus status) {
        return artworkService.getArtworkByArtworkStatus(status);
    }

    @PatchMapping("/change/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> changeStatus(@PathVariable Long id, @RequestParam(required = false) ArtworkStatus status) {
        return artworkService.changeArtworkStatus(id, status);
    }
}
