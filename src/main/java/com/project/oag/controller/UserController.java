package com.project.oag.controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.User;
import com.project.oag.exceptions.UserAlreadyExistException;
import com.project.oag.security.ActiveUserStore;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/users")
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
/*
	@ModelAttribute("user")
    public UserDto userDto() {
        return new UserDto();
    }*
	/*
	@GetMapping("/register")
	public String showRegistrationForm() {
		return "registration";
	}*/
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
	    }*/
	    
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
	    }*/
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
	    }
	    
}
