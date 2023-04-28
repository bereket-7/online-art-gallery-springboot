package com.project.oag.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.project.oag.common.FileUploadUtil;
import com.project.oag.entity.Customer;
import com.project.oag.service.CustomerService;

@RestController
@RequestMapping("/customer")
@CrossOrigin("http://localhost:8080/")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    
    private String path = "src/main/resources/static/img/user-images/";

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer user,
            @RequestParam("image") MultipartFile image) throws IOException {
        String filename = StringUtils.cleanPath(image.getOriginalFilename());
        user.setPhotos(filename);
        customerService.registerUser(user);
    	FileUploadUtil.uploadFile(path, filename, image);
        return ResponseEntity.ok("User registered successfully. Please check your email for confirmation.");
    }
    
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody Customer user) {
        customerService.registerUser(user);
        return ResponseEntity.ok("User registered successfully. Please check your email for confirmation.");
    }
    
    /*
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer user) throws IOException {
        customerService.registerUser(user);
        return ResponseEntity.ok("User registered successfully. Please check your email for confirmation.");
    }
@PostMapping("/upload")
public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile image) throws IOException {
    String filename = StringUtils.cleanPath(image.getOriginalFilename());
    FileUploadUtil.uploadFile(path, filename, image);
    return ResponseEntity.ok(filename);
}


 	@PostMapping("/register")
public ResponseEntity<String> registerUser(@RequestBody Customer user, @RequestParam("filename") String filename) {
    user.setPhotos(filename);
    customerService.registerUser(user);
    return ResponseEntity.ok("User registered successfully. Please check your email for confirmation.");
}
 
    /*
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam String email, @RequestParam String token) {
        customerService.confirmRegistration(email, token);
        return ResponseEntity.ok("Registration confirmed successfully.");
    }*/
  
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam String email, @RequestParam String confirmationCode) {
        try {
            customerService.confirmRegistration(email, confirmationCode);
            return ResponseEntity.ok("Registration confirmed successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
}
