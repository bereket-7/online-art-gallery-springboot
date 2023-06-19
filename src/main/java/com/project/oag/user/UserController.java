package com.project.oag.user;

import java.util.List;

import com.project.oag.artwork.ArtistDTO;
import com.project.oag.user.User;
import com.project.oag.security.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users")
@CrossOrigin("http://localhost:8080/")
public class UserController {
	 private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private String path = "src/main/resources/static/img/user-images/";
	@Autowired
	private CustomUserDetailsService userService;
	   @Autowired
	    private MessageSource messages;
	   
//	    @Autowired
//	    private UserSecurityService userSecurityService;

	   public UserController(CustomUserDetailsService userService) {
		super();
		this.userService = userService;
	}
	@PostMapping("profile/upload")
	public ResponseEntity<String> uploadProfilePhoto(
			@RequestParam("file") MultipartFile file,
			Authentication authentication
	) {
		String loggedInEmail = authentication.getName();
		userService.uploadProfilePhoto(loggedInEmail, file);
		return ResponseEntity.ok("Profile photo uploaded successfully");
	}
	@GetMapping("profile/photo")
	public ResponseEntity<byte[]> getProfilePhoto(Authentication authentication) {
		String loggedInEmail = authentication.getName();
		byte[] photoBytes = userService.getProfilePhoto(loggedInEmail);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(photoBytes, headers, HttpStatus.OK);
	}
	    @GetMapping("/all")
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }

	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok("User deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
		}
	}

	// Reset password
	    /*
	    @PostMapping("/resetPassword")
	    public GenericResponse resetPassword(final HttpServletRequest request,
	            @RequestParam("email") final String userEmail) {
	        final User user = userService.findUserByEmail(userEmail);
	        if (user != null) {
	            final String token = UUID.randomUUID().toString();
	            userService.createPasswordResetTokenForUser(user, token);
	            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
	        }
	        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
	    }*/

	    // Save password
//	    @PostMapping("/savePassword")
//	    public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {
//
//	        final String result = userSecurityService.validatePasswordResetToken(passwordDto.getToken());
//
//	        if (result != null) {
//	            return new GenericResponse(messages.getMessage("auth.message." + result, null, locale));
//	        }
//
//	        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
//	        if (user.isPresent()) {
//	            userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
//	            return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
//	        } else {
//	            return new GenericResponse(messages.getMessage("auth.message.invalid", null, locale));
//	        }
//	    }

//	    // Change user password
//	    @PostMapping("/updatePassword")
//	    public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
//	        final User user = userService.findUserByEmail(
//	                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
//	        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
//	            throw new InvalidOldPasswordException();
//	        }
//	        userService.changeUserPassword(user, passwordDto.getNewPassword());
//	        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
//	    }

	@GetMapping("/total-customer-users")
	public Long getTotalCustomerUsers() {
		return userService.getTotalCustomerUsers();
	}

	@GetMapping("/total-artist-users")
	public Long getTotalArtistUsers() {
		return userService.getTotalArtistUsers();
	}

	@GetMapping("/total-manager-users")
	public Long getTotalManagerUsers() {
		return userService.getTotalManagerUsers();
	}

	@GetMapping("/artist-list")
	public ResponseEntity<List<User>> getArtistUsers() {
		List<User> artistUsers = userService.getArtistUsers();
		return ResponseEntity.ok(artistUsers);
	}
	@GetMapping("/artist-detail")
	public ResponseEntity<List<ArtistDTO>> getArtistDetail() {
		List<ArtistDTO> artistUsers = userService.getArtistDetail();
		return ResponseEntity.ok(artistUsers);
	}


	// ============== NON-API ============

	    private String getAppUrl(HttpServletRequest request) {
	        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	    }

	    private String getClientIP(HttpServletRequest request) {
	        final String xfHeader = request.getHeader("X-Forwarded-For");
	        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
	            return request.getRemoteAddr();
	        }
	        return xfHeader.split(",")[0];
	    }
	    
}