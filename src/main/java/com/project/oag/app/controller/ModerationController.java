package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.dto.RejectionReasonDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.app.service.AuditLogService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/moderation")
@Tag(name = "Moderation")
public class ModerationController {

    private final ArtworkService artworkService;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public ModerationController(ArtworkService artworkService, AuditLogService auditLogService,
                                UserRepository userRepository) {
        this.artworkService = artworkService;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
    }

    @GetMapping("/queue")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
    public ResponseEntity<GenericResponse> queue() {
        return prepareResponse(HttpStatus.OK, "Moderation queue",
                artworkService.getArtworkByStatus(ArtworkStatus.PENDING));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> approve(@PathVariable Long id, HttpServletRequest request) {
        var result = artworkService.changeArtworkStatus(id, ArtworkStatus.ACCEPTED);
        auditLogService.log(resolveAdminId(request), "CHANGE_ARTWORK_STATUS", "Artwork", id, "Status set to ACCEPTED");
        return prepareResponse(HttpStatus.OK, "Artwork approved", result);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
    public ResponseEntity<GenericResponse> reject(@PathVariable Long id,
                                                  HttpServletRequest request,
                                                  @RequestBody(required = false) RejectionReasonDto body) {
        String reason = body != null ? body.getRejectionReason() : null;
        var result = artworkService.changeArtworkStatus(id, ArtworkStatus.REJECTED, reason);
        auditLogService.log(resolveAdminId(request), "CHANGE_ARTWORK_STATUS", "Artwork", id, "Status set to REJECTED");
        return prepareResponse(HttpStatus.OK, "Artwork rejected", result);
    }

    private Long resolveAdminId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
