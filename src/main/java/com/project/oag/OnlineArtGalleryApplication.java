package com.project.oag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:8080")
public class OnlineArtGalleryApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlineArtGalleryApplication.class, args);
	}
}
