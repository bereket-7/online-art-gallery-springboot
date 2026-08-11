package com.project.oag.app.controller;

import com.project.oag.app.entity.Rating;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.RatingService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/ratings")
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
                                                       @RequestParam Long artworkId,
                                                       @RequestParam double ratingValue) {
        Rating rating = ratingService.rateArtwork(resolveUserId(request), artworkId, ratingValue);
        return prepareResponse(HttpStatus.CREATED, "Rating saved", rating);
    }

    @GetMapping("/artwork/{artworkId}")
    public ResponseEntity<GenericResponse> getRatings(@PathVariable Long artworkId) {
        return prepareResponse(HttpStatus.OK, "Ratings retrieved", ratingService.getRatingsForArtwork(artworkId));
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
