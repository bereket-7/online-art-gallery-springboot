package com.project.oag.controller;

import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;
import com.project.oag.entity.User;
import com.project.oag.security.ActiveUserStore;
import com.project.oag.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:8080/")
public class UserController {
	@Autowired
	private UserService userService;
	
	   @Autowired
	    ActiveUserStore activeUserStore;

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
	    
	    @GetMapping("/all")
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }
	    
	    @PostMapping("/add")
	    public User addUser(@RequestBody User user) {
	        return userService.addUser(user);
	    }
	   
	    /*
	    @DeleteMapping("/{id}")
	    public void deleteUser(@PathVariable Long id) {
	        userService.deleteUser(id);
	    }*/
	    
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
	   /* 
	    @PostMapping("/login")
	    public ResponseEntity<String> login(@RequestBody User user) {
	        Optional<User> optionalCustomer = userRepository.findByUsernameAndPassword(
	                user.getFirstname(), user.getPassword());
	        if (optionalCustomer.isPresent()) {
	            User authenticatedUser = optionalCustomer.get();
	            String role = authenticatedUser.getRoles();
	            if (role.equals("admin")) {
	                // Redirect to admin page
	                return new ResponseEntity<>("Redirecting to admin page", HttpStatus.OK);
	            } else if (role.equals("manager")) {
	                // Redirect to manager page
	                return new ResponseEntity<>("Redirecting to manager page", HttpStatus.OK);
	            } else if (role.equals("artist")) {
	                // Redirect to artist page
	                return new ResponseEntity<>("Redirecting to artist page", HttpStatus.OK);
	            } else if (role.equals("customer")) {
	                // Redirect to customer page
	                return new ResponseEntity<>("Redirecting to customer page", HttpStatus.OK);
	            }
	        }
	        // Login failed
	        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
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
	        return "redirect:/login?logout"; //localhost:8080/login.vue
	    }
	    
}
