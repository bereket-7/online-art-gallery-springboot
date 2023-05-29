package com.project.oag.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.common.ApiResponse;
import com.project.oag.common.GenericResponse;
import com.project.oag.controller.dto.PasswordDto;
import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.Role;
import com.project.oag.entity.User;
import com.project.oag.exceptions.InvalidOldPasswordException;
import com.project.oag.exceptions.UserAlreadyExistException;
import com.project.oag.security.ActiveUserStore;
import com.project.oag.security.UserSecurityService;
import com.project.oag.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:8080/")
public class UserController {
	 private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private String path = "src/main/resources/static/img/user-images/";
	@Autowired
	private UserService userService;
	
	   @Autowired
	    ActiveUserStore activeUserStore;
	   
	   @Autowired
	    private MessageSource messages;
	   
	    @Autowired
	    private UserSecurityService userSecurityService;

	   public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	    @GetMapping("/loggedUsers")
	    public String getLoggedUsers(final Locale locale, final Model model) {
	        model.addAttribute("users", activeUserStore.getUsers());
	        return "users";
	    }

	    @GetMapping("/loggedUsersFromSessionRegistry")
	    public String getLoggedUsersFromSessionRegistry(final Locale locale, final Model model) {
	        model.addAttribute("users", userService.getUsersFromSessionRegistry());
	        return "users";
	    }	    
	    
	    @PostMapping("/{userId}/uploadProfilePhoto")
	    public ApiResponse uploadProfilePhoto(@PathVariable Long userId, @RequestParam("image") MultipartFile image) {
	        try {
	            userService.uploadProfilePhoto(userId, image);
	            return new ApiResponse(true, "Profile photo uploaded successfully");
	        } catch (EntityNotFoundException e) {
	            return new ApiResponse(false, e.getMessage());
	        } catch (IOException e) {
	            return new ApiResponse(false, "Failed to upload profile photo");
	        }
	    }
//	    
//	    @PostMapping("/confirm-registration")
//	    public ApiResponse confirmRegistration(@RequestBody Map<String, String> request) {
//	        String email = request.get("email");
//	        String confirmationCode = request.get("confirmationCode");
//	        userService.confirmRegistration(email, confirmationCode);
//	        return new ApiResponse(true, "Registration confirmed successfully.");
//	    }    
	    
//	    @PostMapping("/send-confirm/{email}")
//	    public ResponseEntity<ApiResponse> sendConfirmationEmail(@RequestParam String email) {
//	        userService.sendConfirmationEmail(email);
//	        ApiResponse response = new ApiResponse(true, "Confirmation email sent successfully");
//	        return ResponseEntity.ok(response);
//	    }
   
//	    @PostMapping("/send-confirm")
//	    public ResponseEntity<ApiResponse> sendConfirmationEmail(@RequestParam(required = false) String email, @RequestBody(required = false) Map<String, String> requestBody) {
//	        String emailParameter = null;
//
//	        if (email != null) {
//	            emailParameter = email;
//	        } else if (requestBody != null && requestBody.containsKey("email")) {
//	            emailParameter = requestBody.get("email");
//	        }
//
//	        if (emailParameter == null) {
//	            throw new IllegalArgumentException("Email is required");
//	        }
//
//	        userService.sendConfirmationEmail(emailParameter);
//	        ApiResponse response = new ApiResponse(true, "Confirmation email sent successfully");
//	        return ResponseEntity.ok(response);
//	    }

	   
//	    @PostMapping("/signup")
//	    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
//	        try {
//	            userService.registerUser(userDto);
//	            return ResponseEntity.status(HttpStatus.CREATED).build();
//	        } catch (UserAlreadyExistException e) {
//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//	        } catch (NonUniqueResultException ex) {
//	            return ResponseEntity.badRequest().body("There is an account with that email address: " + userDto.getEmail());
//	        }
//	    }

	    
	    /*
	    @PostMapping("/confirm")
	    public ResponseEntity<String> confirmRegistration(@RequestParam String email, @RequestParam String confirmationCode) {
	        try {
	            userService.confirmRegistration(email, confirmationCode);
	            return ResponseEntity.ok("Registration confirmed successfully!");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }*/
	    
	    @GetMapping("/all")
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }
	    
	    @PostMapping("/add")
	    public User addUser(@RequestBody User user) {
	        return userService.addUser(user);
	    }
	    
	    @PutMapping("/{id}")
	    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
	        return userService.updateUser(id, updatedUser);
	    }
	    
	    @GetMapping("/{id}")
	    public User getUserById(@PathVariable Long id) {
	        return userService.getUserById(id);
	    }
	    
	    @GetMapping("/admins")
	    public List<User> getAdminUsers() {
	        return userService.getUsersByRole("ADMIN");
	    }
	    
	    @GetMapping("/users")
	    public List<User> getNormalUsers() {
	        return userService.getUsersByRole("USER");
	    }
	    
	    @GetMapping("/artists")
	    public List<User> getArtistUsers() {
	        return userService.getUsersByRole("ARTIST");
	    }
	    
	    @GetMapping("/managers")
	    public List<User> getManagerUsers() {
	        return userService.getUsersByRole("MANAGER");
	    }
	    
	    @PostMapping("/logout")
	    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	        
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null) {
	            new SecurityContextLogoutHandler().logout(request, response, auth);
	        }
	        return "redirect:/login?logout"; //localhost:8080/login.vue
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
	    @PostMapping("/savePassword")
	    public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {

	        final String result = userSecurityService.validatePasswordResetToken(passwordDto.getToken());

	        if (result != null) {
	            return new GenericResponse(messages.getMessage("auth.message." + result, null, locale));
	        }

	        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
	        if (user.isPresent()) {
	            userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
	            return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
	        } else {
	            return new GenericResponse(messages.getMessage("auth.message.invalid", null, locale));
	        }
	    }

	    // Change user password
	    @PostMapping("/updatePassword")
	    public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
	        final User user = userService.findUserByEmail(
	                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
	        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
	            throw new InvalidOldPasswordException();
	        }
	        userService.changeUserPassword(user, passwordDto.getNewPassword());
	        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
	    }

	    // Change user 2 factor authentication
	    @PostMapping("/update/2fa")
	    public GenericResponse modifyUser2FA(@RequestParam("use2FA") final boolean use2FA)
	            throws UnsupportedEncodingException {
	        final User user = userService.updateUser2FA(use2FA);
	        if (use2FA) {
	            return new GenericResponse(userService.generateQRUrl(user));
	        }
	        return null;
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
