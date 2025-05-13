package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.dto.GenericResponsePageable;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.project.oag.common.AppConstants.*;
import static com.project.oag.utils.RequestUtils.getPageable;

@RestController
@RequestMapping("api/v1/user/artwork")
public class ArtworkController {
    private final ArtworkService artworkService;
    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<GenericResponse> saveArtwork(HttpServletRequest request, @RequestBody ArtworkRequestDto artworkRequestDto) throws IOException {
        return artworkService.saveArtwork(request, artworkRequestDto);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<GenericResponsePageable> getRecentArtworks(@RequestParam(value = "sortType", defaultValue = LAST_UPDATE_DATE_DESC, required = false) List<String> sortType,
                                                                     @RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                                                     @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Pageable pageable = getPageable(sortType, pageNumber, pageSize);
        return artworkService.getRecentArtworks(pageable);
    }

    @GetMapping("/category/count")
    public ResponseEntity<GenericResponse> getCountByCategory() {
        return artworkService.getCountByCategory();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CUSTOMER')")
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

    @GetMapping("/myArtwork")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<GenericResponse> getLoggedArtistArtworks(HttpServletRequest request) {
        return artworkService.getLoggedArtistArtworks(request);
    }
}
