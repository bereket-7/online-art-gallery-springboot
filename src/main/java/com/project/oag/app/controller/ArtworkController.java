package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.project.oag.common.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.project.oag.common.AppConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("api/v1/artwork")
public class ArtworkController {
    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('USER_ADD_ARTWORK')")
    public ResponseEntity<GenericResponse> saveArtwork(HttpServletRequest request, @RequestBody ArtworkRequestDto artworkRequestDto) throws IOException {
        return artworkService.saveArtwork(request, artworkRequestDto);
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
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> changeStatus(@PathVariable Long id, @RequestParam(required = false) ArtworkStatus status) {
        return artworkService.changeArtworkStatus(id, status);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAuthority('USER_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getRecentArtworks() {
        return artworkService.getRecentArtworks();
    }

    @GetMapping("/category/count")
    public ResponseEntity<GenericResponse> getCountByCategory() {
        return artworkService.getCountByCategory();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> searchAndFilterArtwork(
            @RequestParam(required = false) String artworkCategory,
            @RequestParam(required = false) String artworkName,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE, required = false) int size) {
        return artworkService.searchArtwork(artworkCategory, artworkName, minPrice, maxPrice, sortBy, fromDate, toDate, PageRequest.of(page, size));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> getLoggedArtistArtworks(HttpServletRequest request) {
        return artworkService.getLoggedArtistArtworks(request);
    }
}
