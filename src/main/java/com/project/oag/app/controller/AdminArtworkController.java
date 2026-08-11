package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.app.service.AuditLogService;
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
@RequestMapping("api/v1/admin/artwork")
public class AdminArtworkController {

    private final ArtworkService artworkService;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public AdminArtworkController(ArtworkService artworkService,
                                  AuditLogService auditLogService,
                                  UserRepository userRepository) {
        this.artworkService = artworkService;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
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
    public ResponseEntity<GenericResponse> deleteArtwork(@PathVariable Long id, HttpServletRequest request) {
        artworkService.deleteArtwork(id);
        auditLogService.log(resolveAdminId(request), "DELETE_ARTWORK", "Artwork", id, "Artwork deleted");
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
                                                        @RequestParam ArtworkStatus status,
                                                        HttpServletRequest request) {
        var result = artworkService.changeArtworkStatus(id, status);
        auditLogService.log(resolveAdminId(request), "CHANGE_ARTWORK_STATUS", "Artwork", id, "Status set to " + status);
        return prepareResponse(HttpStatus.OK, "Artwork status updated", result);
    }

    private Long resolveAdminId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
