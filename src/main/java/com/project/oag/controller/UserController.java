package com.project.oag.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.User;
import com.project.oag.security.ActiveUserStore;
import com.project.oag.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	   @Autowired
	    ActiveUserStore activeUserStore;
	  //@Autowired
	  //private EmailService emailService;

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
	    
	    @GetMapping("/users/all")
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }
	/*@PostMapping("/register_user")
	public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) throws UserAlreadyExistException{
		User user = userService.registerNewUser(userDto);
		return ResponseEntity.ok(user);
	}

	    /*@PostMapping("/verify-email")
	    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String token)
	            throws Throwable {
	        userService.verifyEmail(email, token);
	        return new ResponseEntity<>("Email verified successfully", HttpStatus.OK);
	    }
	    
	    @GetMapping
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }
	    
	    @GetMapping("/{id}")
	    public ResponseEntity<User> getUserById(@PathVariable Long id) {
	        Optional<User> user = userService.getUserById(id);
	        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	   /* @PutMapping("/{id}")
	    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws UserNotFoundException {
	        User existingUser = userService.updateUser(id, userDto);
	    	return ResponseEntity.ok(existingUser);
	        //return existingUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }
	    @DeleteMapping("/{id}")
	    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
	        userService.deleteUser(id);
	        return ResponseEntity.noContent().build();
	    }
	    
	    @PostMapping("/logout")
	    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	        
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null) {
	            new SecurityContextLogoutHandler().logout(request, response, auth);
	        }
	        return "redirect:/login?logout";
	    }*/
	    
}
