package com.project.oag.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam String email, @RequestParam String token) {
        customerService.confirmRegistration(email, token);
        return ResponseEntity.ok("Registration confirmed successfully.");
    }
    
}
