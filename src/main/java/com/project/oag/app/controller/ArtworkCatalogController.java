package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.GenericResponsePageable;
import com.project.oag.app.dto.PageableDto;
import com.project.oag.app.dto.ReviewRequestDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.app.service.DiscoveryService;
import com.project.oag.app.service.RatingService;
import com.project.oag.common.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

import static com.project.oag.common.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.project.oag.common.AppConstants.DEFAULT_PAGE_SIZE;
import static com.project.oag.common.AppConstants.LAST_UPDATE_DATE_DESC;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.RequestUtils.getPageable;
import static com.project.oag.utils.Utils.prepareResponse;
import static com.project.oag.utils.Utils.prepareResponseWithPageable;

@RestController
@RequestMapping("api/v1/artworks")
@Tag(name = "Artworks")
public class ArtworkCatalogController {

    private final ArtworkService artworkService;
    private final UserRepository userRepository;
    private final DiscoveryService discoveryService;
    private final RatingService ratingService;

    public ArtworkCatalogController(ArtworkService artworkService, UserRepository userRepository,
                                    DiscoveryService discoveryService, RatingService ratingService) {
        this.artworkService = artworkService;
        this.userRepository = userRepository;
        this.discoveryService = discoveryService;
        this.ratingService = ratingService;
    }

    @GetMapping
    public ResponseEntity<GenericResponsePageable> listAccepted(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(required = false) String artworkCategory,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortBy) {
        Map.Entry<List<ArtworkResponseDto>, PageableDto> result = artworkService.searchArtwork(
                artworkCategory, null, minPrice, maxPrice, sortBy, null, null, PageRequest.of(page, size));
        return prepareResponseWithPageable(HttpStatus.OK, "Artworks retrieved", result.getKey(), result.getValue());
    }

    @GetMapping("/search")
    public ResponseEntity<GenericResponsePageable> search(
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
        return prepareResponseWithPageable(HttpStatus.OK, "Search results", result.getKey(), result.getValue());
    }

    @GetMapping("/recent")
    public ResponseEntity<GenericResponsePageable> recent(
            @RequestParam(value = "sortType", defaultValue = LAST_UPDATE_DATE_DESC, required = false) List<String> sortType,
            @RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        Pageable pageable = getPageable(sortType, pageNumber, pageSize);
        Map.Entry<List<ArtworkResponseDto>, PageableDto> result = artworkService.getRecentArtworks(pageable);
        return prepareResponseWithPageable(HttpStatus.OK, "Successfully retrieved recent artworks",
                result.getKey(), result.getValue());
    }

    @GetMapping("/trending")
    public ResponseEntity<GenericResponse> trending(@RequestParam(defaultValue = "10") int limit) {
        return prepareResponse(HttpStatus.OK, "Trending artworks", discoveryService.getTrendingArtworks(limit));
    }

    @GetMapping("/category/count")
    public ResponseEntity<GenericResponse> categoryCount() {
        return prepareResponse(HttpStatus.OK, "Category counts retrieved", artworkService.getCountByCategory());
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('ARTIST_VIEW_OWN_ARTWORK')")
    public ResponseEntity<GenericResponse> mine(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Artworks retrieved", artworkService.getLoggedArtistArtworks(request));
    }

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ARTIST_SUBMIT_ARTWORK')")
    public ResponseEntity<GenericResponse> submit(HttpServletRequest request,
                                                  @ModelAttribute ArtworkRequestDto artworkRequestDto) throws IOException {
        return prepareResponse(HttpStatus.CREATED, "Artwork submitted successfully",
                artworkService.saveArtwork(request, artworkRequestDto));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<GenericResponse> reviews(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Reviews retrieved", ratingService.getReviewsForArtwork(id));
    }

    @PostMapping("/{id}/reviews")
    @PreAuthorize("hasAuthority('USER_RATE_ARTWORK')")
    public ResponseEntity<GenericResponse> addReview(@PathVariable Long id,
                                                     HttpServletRequest request,
                                                     @Valid @RequestBody ReviewRequestDto dto) {
        Long userId = resolveUserId(request);
        return prepareResponse(HttpStatus.CREATED, "Review saved",
                ratingService.rateArtwork(userId, id, dto.getRating(), dto.getComment()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getById(@PathVariable Long id, HttpServletRequest request) {
        Long viewerId = resolveOptionalUserId(request);
        return prepareResponse(HttpStatus.OK, "Artwork retrieved", artworkService.getArtworkById(id, viewerId));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ARTIST_VIEW_OWN_ARTWORK', 'ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> update(@PathVariable Long id, @RequestBody ArtworkRequestDto dto) {
        return prepareResponse(HttpStatus.OK, "Artwork updated", artworkService.updateArtwork(id, dto));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        return userRepository.findByEmailIgnoreCase(email).map(User::getId)
                .orElseThrow(() -> new com.project.oag.exceptions.UserNotFoundException("User not found: " + email));
    }

    private Long resolveOptionalUserId(HttpServletRequest request) {
        try {
            return resolveUserId(request);
        } catch (Exception e) {
            return null;
        }
    }
}
