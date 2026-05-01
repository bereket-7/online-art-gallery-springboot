package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.GenericResponsePageable;
import com.project.oag.app.dto.PageableDto;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.project.oag.common.AppConstants.*;
import static com.project.oag.utils.RequestUtils.getPageable;
import static com.project.oag.utils.Utils.prepareResponse;
import static com.project.oag.utils.Utils.prepareResponseWithPageable;

@RestController
@RequestMapping("api/v1/user/artwork")
public class ArtworkController {

    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    /**
     * Artists submit their own artwork for admin review.
     * Requires ARTIST role — not CUSTOMER.
     */
    @PostMapping(value = "/submit", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ARTIST_SUBMIT_ARTWORK')")
    public ResponseEntity<GenericResponse> saveArtwork(HttpServletRequest request,
                                                       @ModelAttribute ArtworkRequestDto artworkRequestDto) throws IOException {
        ArtworkResponseDto result = artworkService.saveArtwork(request, artworkRequestDto);
        return prepareResponse(HttpStatus.CREATED, "Artwork submitted successfully", result);
    }

    /**
     * Browsing recent artworks — open to both customers and artists.
     */
    @GetMapping("/recent")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_BROWSE_ARTWORK', 'ARTIST_BROWSE_ARTWORK')")
    public ResponseEntity<GenericResponsePageable> getRecentArtworks(
            @RequestParam(value = "sortType", defaultValue = LAST_UPDATE_DATE_DESC, required = false) List<String> sortType,
            @RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Pageable pageable = getPageable(sortType, pageNumber, pageSize);
        Map.Entry<List<ArtworkResponseDto>, PageableDto> result = artworkService.getRecentArtworks(pageable);
        return prepareResponseWithPageable(HttpStatus.OK, "Successfully retrieved recent artworks",
                result.getKey(), result.getValue());
    }

    @GetMapping("/category/count")
    public ResponseEntity<GenericResponse> getCountByCategory() {
        return prepareResponse(HttpStatus.OK, "Category counts retrieved", artworkService.getCountByCategory());
    }

    /**
     * Artwork search — open to both customers and artists.
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_BROWSE_ARTWORK', 'ARTIST_BROWSE_ARTWORK')")
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
        Map.Entry<List<ArtworkResponseDto>, PageableDto> result = artworkService.searchArtwork(
                artworkCategory, artworkName, minPrice, maxPrice, sortBy, fromDate, toDate, PageRequest.of(page, size));
        return prepareResponse(HttpStatus.OK, "Search results", result.getKey());
    }

    /**
     * Artist views their own submitted artworks.
     * Requires ARTIST role — not CUSTOMER.
     */
    @GetMapping("/myArtwork")
    @PreAuthorize("hasAuthority('ARTIST_VIEW_OWN_ARTWORK')")
    public ResponseEntity<GenericResponse> getLoggedArtistArtworks(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Artworks retrieved", artworkService.getLoggedArtistArtworks(request));
    }
}
