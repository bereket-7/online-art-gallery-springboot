package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtistDTO;
import com.project.oag.app.dto.ChangePasswordRequest;
import com.project.oag.app.model.User;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.IncorrectPasswordException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.security.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class  UserController {
	//private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    //private String path = "src/main/resources/static/img/user-images/";
	private final CustomUserDetailsService userService;
    public UserController(CustomUserDetailsService userService) {
        this.userService = userService;

    }

    @PostMapping("profile/upload")
	@PreAuthorize("hasAuthority('USER_MODIFY_PROFILE')")
	public ResponseEntity<GenericResponse> uploadProfilePhoto(HttpServletRequest request, @RequestParam("photoUrl") String photoUrl) {
		return userService.uploadProfilePhoto(request,photoUrl);
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
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ADMIN_FETCH_USER')")
	public List<User> getAllUsers() {
	        return userService.getAllUsers();
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

	// Reset password
	@PostMapping("password/request")
	public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) {
		userService.initiatePasswordReset(email);
		return ResponseEntity.ok("Password reset email sent check your email.");
	}
	@PostMapping("password/reset")
	public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
		userService.resetPassword(token, newPassword);
		return ResponseEntity.ok("Password reset successfully.");
	}
	@PostMapping("/password/change")
	public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangePasswordRequest request) {
		try {
			String email = userDetails.getUsername();
			userService.changePassword(email, request);
			return ResponseEntity.ok("Password changed successfully.");
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IncorrectPasswordException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	@GetMapping("/total/customer/users")
	@PreAuthorize("hasRole('ADMIN','MANAGER')")
	public Long getTotalCustomerUsers() {
		return userService.getTotalCustomerUsers();
	}
	@GetMapping("/total/artist/users")
	@PreAuthorize("hasRole('ADMIN')")
	public Long getTotalArtistUsers() {
		return userService.getTotalArtistUsers();
	}
	@GetMapping("/total/manager/users")
	@PreAuthorize("hasRole('ADMIN')")
	public Long getTotalManagerUsers() {
		return userService.getTotalManagerUsers();
	}
	@GetMapping("/artist/list")
	public ResponseEntity<List<User>> getArtistUsers() {
		List<User> artistUsers = userService.getArtistUsers();
		return ResponseEntity.ok(artistUsers);
	}
	@GetMapping("/manager/list")
	public ResponseEntity<List<User>> getManagerUsers() {
		List<User> managerUsers = userService.getManagerUsers();
		return ResponseEntity.ok(managerUsers);
	}
	@GetMapping("/customer/list")
	public ResponseEntity<List<User>> getCustomerUsers() {
		List<User> customerUsers = userService.getCustomerUsers();
		return ResponseEntity.ok(customerUsers);
	}
	@GetMapping("/organization/list")
	public ResponseEntity<List<User>> getOrganizationUsers() {
		List<User> organizationUsersUsers = userService.getOrganizationUsers();
		return ResponseEntity.ok(organizationUsersUsers);
	}
	@GetMapping("/artist/detail")
	@PreAuthorize("hasRole('MANAGER','ADMIN')")
	public ResponseEntity<List<ArtistDTO>> getArtistDetail() {
		List<ArtistDTO> artistUsers = userService.getArtistDetail();
		return ResponseEntity.ok(artistUsers);
	}
}