package com.project.oag.utils;

import com.project.oag.app.dto.StandardType;
import com.project.oag.app.entity.Standard;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.StandardRepository;
import com.project.oag.app.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataSeederService {

    private final StandardRepository standardRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeederService(StandardRepository standardRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.standardRepository = standardRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedData() {
        seedStandards();
        seedUsers();
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
                user.setUsername("testuser" + i);
                user.setPassword(passwordEncoder.encode("password" + i));
                user.setEnabled(true);
                user.setVerified(true);
                users.add(user);
            }
            userRepository.saveAll(users);
        }
    }
}