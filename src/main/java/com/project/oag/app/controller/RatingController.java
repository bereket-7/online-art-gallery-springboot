package com.project.oag.app.controller;

import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.dto.RatingRequestDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.RatingService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/ratings")
@Tag(name = "Reviews")
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;

    public RatingController(RatingService ratingService, UserRepository userRepository) {
        this.ratingService = ratingService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_RATE_ARTWORK')")
    public ResponseEntity<GenericResponse> rateArtwork(HttpServletRequest request,
                                                       @Valid @RequestBody RatingRequestDto dto) {
        return prepareResponse(HttpStatus.CREATED, "Rating saved",
                ratingService.rateArtwork(resolveUserId(request), dto.getArtworkId(), dto.getRatingValue(), dto.getComment()));
    }

    @GetMapping("/artwork/{artworkId}")
    public ResponseEntity<GenericResponse> getRatings(@PathVariable Long artworkId) {
        return prepareResponse(HttpStatus.OK, "Ratings retrieved",
                ratingService.getRatingsForArtwork(artworkId).stream()
                        .map(CommerceMappers::toReviewDto)
                        .toList());
    }

    @GetMapping("/artwork/{artworkId}/average")
    public ResponseEntity<GenericResponse> getAverageRating(@PathVariable Long artworkId) {
        return prepareResponse(HttpStatus.OK, "Average rating", ratingService.getAverageRating(artworkId));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
