package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtistDTO;
import com.project.oag.app.entity.User;
import com.project.oag.common.GenericResponse;
import com.project.oag.app.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final CustomUserDetailsService userService;

    public UserController(CustomUserDetailsService userService) {
        this.userService = userService;

    }

    @PostMapping("profile/upload")
    @PreAuthorize("hasAuthority('USER_MODIFY_PROFILE')")
    public ResponseEntity<GenericResponse> uploadProfilePhoto(HttpServletRequest request, @RequestParam("photoUrl") String photoUrl) {
        return userService.uploadProfilePhoto(request, photoUrl);
    }

    @GetMapping("profile/photo")
    @PreAuthorize("hasAuthority('USER_MODIFY_PROFILE')")
    public ResponseEntity<GenericResponse> getProfilePhoto(HttpServletRequest request) {
        return userService.getProfilePhoto(request);
    }

    @GetMapping("/search")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> searchUsersByUsername(@RequestParam("username") String username) {
        List<User> searchResults = userService.searchUsersByUsername(username);
        return ResponseEntity.ok(searchResults);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_USER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    @GetMapping("/total/artist/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long getTotalNumberUser(@RequestParam String roleName) {
        return userService.getTotalArtistUsers(roleName);
    }

//    @GetMapping("/artist/list")
//    public ResponseEntity<List<User>> getArtistUsers(@RequestParam(required = false) String roleName) {
//        List<User> artistUsers = userService.getArtistUsers(roleName);
//    }

//    @GetMapping("/artist/detail")
//    @PreAuthorize("hasRole('MANAGER','ADMIN')")
//    public ResponseEntity<List<ArtistDTO>> getArtistDetail() {
//        List<ArtistDTO> artistUsers = userService.getArtistDetail();
//        return ResponseEntity.ok(artistUsers);
//    }
}