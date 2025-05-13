package com.project.oag.utils;

import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.dto.StandardType;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Standard;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.UserRole;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.RoleRepository;
import com.project.oag.app.repository.StandardRepository;
import com.project.oag.app.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSeederService {

    private final StandardRepository standardRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository userRoleRepository;
    private final ArtworkRepository artworkRepository;

    public DataSeederService(StandardRepository standardRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository userRoleRepository, ArtworkRepository artworkRepository) {
        this.standardRepository = standardRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.artworkRepository = artworkRepository;
    }

    @PostConstruct
    public void seedData() {
        seedStandards();
        seedUsers();
        seedArtworks();
    }
    private void seedStandards() {
        if (standardRepository.count() < 15) {
            List<Standard> standards = new ArrayList<>();
            StandardType[] types = StandardType.values();
            for (int i = 1; i <= 20; i++) {
                Standard standard = new Standard();
                standard.setStandardDescription("Standard Description " + i);
                standard.setStandardType(types[i % types.length]);
                standards.add(standard);
            }
            standardRepository.saveAll(standards);
        }
    }

    private void seedUsers() {
        if (userRepository.count() < 50) {
            List<User> users = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                User user = new User();
                user.setFirstName("TestFirstName" + i);
                user.setLastName("TestLastName" + i);
                user.setEmail("testuser" + i + "@example.com");
                user.setPassword(passwordEncoder.encode("password" + i));
                user.setPhone("123456789" + i);
                user.setAddress("Test Address " + i);
                user.setSex(i % 2 == 0 ? "Male" : "Female");
                user.setAge(20 + (i % 10));
                user.setImage("avatar" + i + ".png");
                user.setSelectedForBid(false);
                user.setResetPasswordToken(null);
                user.setUserRole(getDefaultUserRole());
                users.add(user);
            }
            userRepository.saveAll(users);
        }
    }

    private UserRole getDefaultUserRole() {
        val userRole = userRoleRepository.findByRoleNameIgnoreCase("ROLE_CUSTOMER").
                orElse(null);
        return userRole;
    }

    private void seedArtworks() {
        if (artworkRepository.count() < 30) {
            List<Artwork> artworks = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                Artwork artwork = new Artwork();
                artwork.setArtworkName("Artwork Name " + i);
                artwork.setArtworkDescription("Description for Artwork " + i);
                artwork.setArtworkCategory(i % 2 == 0 ? "Painting" : "Sculpture");
                artwork.setImageUrls(List.of("image" + i + "_1.jpg", "image" + i + "_2.jpg"));
                artwork.setPrice(BigDecimal.valueOf(100 + (i * 10)));
                artwork.setSize(i % 2 == 0 ? "Medium" : "Large");
                artwork.setStatus(i % 2 == 0 ? ArtworkStatus.ACCEPTED : ArtworkStatus.PENDING);
                artwork.setQuantity(10 + (i % 5));
                artwork.setUser(getDefaultUser());
                artworks.add(artwork);
            }
            artworkRepository.saveAll(artworks);
        }
    }

    private User getDefaultUser() {
        return userRepository.findByEmailIgnoreCase("defaultuser@example.com")
                .orElseGet(() -> {
                    User user = new User();
                    user.setFirstName("Default");
                    user.setLastName("User");
                    user.setEmail("defaultuser@example.com");
                    user.setPassword(passwordEncoder.encode("password"));
                    user.setUserRole(getDefaultUserRole());
                    return userRepository.save(user);
                });
    }
}