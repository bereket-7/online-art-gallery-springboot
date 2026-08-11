package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtistProfileDto;
import com.project.oag.app.dto.GenericResponsePageable;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.ArtistFollowService;
import com.project.oag.app.service.ArtistProfileService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.common.AppConstants.DEFAULT_PAGE_NUMBER;
import static com.project.oag.common.AppConstants.DEFAULT_PAGE_SIZE;
import static com.project.oag.utils.PageableUtils.preparePageInfo;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;
import static com.project.oag.utils.Utils.prepareResponseWithPageable;

@RestController
@RequestMapping("api/v1/artists")
public class ArtistProfileController {

    private final ArtistProfileService artistProfileService;
    private final ArtistFollowService artistFollowService;
    private final UserRepository userRepository;

    public ArtistProfileController(ArtistProfileService artistProfileService,
                                   ArtistFollowService artistFollowService,
                                   UserRepository userRepository) {
        this.artistProfileService = artistProfileService;
        this.artistFollowService = artistFollowService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getArtistProfile(@PathVariable Long id) {
        ArtistProfileDto profile = artistProfileService.getArtistProfile(id);
        return prepareResponse(HttpStatus.OK, "Artist Profile retrieved", profile);
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<GenericResponse> getArtistProfileByUuid(@PathVariable String uuid) {
        ArtistProfileDto profile = artistProfileService.getArtistProfileByUuid(uuid);
        return prepareResponse(HttpStatus.OK, "Artist Profile retrieved", profile);
    }

    @GetMapping("/{id}/portfolio")
    public ResponseEntity<GenericResponsePageable> getArtistPortfolio(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        var portfolio = artistProfileService.getArtistPortfolio(id, PageRequest.of(page, size));
        return prepareResponseWithPageable(HttpStatus.OK, "Portfolio retrieved", portfolio.getContent(), preparePageInfo(portfolio));
    }

    @PostMapping("/{id}/follow")
    @PreAuthorize("hasAuthority('USER_FOLLOW_ARTIST')")
    public ResponseEntity<GenericResponse> followArtist(@PathVariable Long id, HttpServletRequest request) {
        artistFollowService.follow(resolveUserId(request), id);
        return prepareResponse(HttpStatus.CREATED, "Artist followed", null);
    }

    @DeleteMapping("/{id}/follow")
    @PreAuthorize("hasAuthority('USER_FOLLOW_ARTIST')")
    public ResponseEntity<GenericResponse> unfollowArtist(@PathVariable Long id, HttpServletRequest request) {
        artistFollowService.unfollow(resolveUserId(request), id);
        return prepareResponse(HttpStatus.OK, "Artist unfollowed", null);
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
