package com.project.oag.security.service;

import com.project.oag.entity.PasswordResetToken;
import com.project.oag.entity.Role;
import com.project.oag.entity.User;
import com.project.oag.registration.token.ConfirmationToken;
import com.project.oag.registration.token.ConfirmationTokenService;
import com.project.oag.repository.PasswordResetTokenRepository;
import com.project.oag.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Configuration
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository,
                                    ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
    }
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("user Not found "));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String signUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException("email already registered");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }
    public void uploadProfilePhoto(String loggedInEmail, MultipartFile file) {
        Optional<User> optionalUser = userRepository.findByEmail(loggedInEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                user.setImage(file.getBytes());
                userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload profile photo");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public byte[] getProfilePhoto(String loggedInEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(loggedInEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getImage();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //password reset
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }
//    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
//        return passwordEncoder.matches(oldPassword, user.getPassword());
//    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Long getTotalCustomerUsers() {
        return userRepository.countTotalUsersByRole(Role.CUSTOMER);
    }
    public Long getTotalArtistUsers() {
        return userRepository.countTotalUsersByRole(Role.ARTIST);
    }
    public Long getTotalManagerUsers() {
        return userRepository.countTotalUsersByRole(Role.MANAGER);
    }
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }
}