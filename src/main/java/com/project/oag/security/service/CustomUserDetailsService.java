package com.project.oag.security.service;

import com.project.oag.app.dto.*;
import com.project.oag.app.model.PasswordResetToken;
import com.project.oag.app.model.User;
import com.project.oag.app.repository.PasswordResetTokenRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.EmailService;
import com.project.oag.exceptions.IncorrectPasswordException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.registration.token.ConfirmationToken;
import com.project.oag.registration.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Configuration
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    public CustomUserDetailsService(UserRepository userRepository,
                                    ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
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
            throw new IllegalStateException("This Email already registered");
        }
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
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
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

    public List<User> getArtistUsers() {
        return userRepository.findByRole(Role.ARTIST);
    }
    public List<User> getManagerUsers() {
        return userRepository.findByRole(Role.MANAGER);
    }
    public List<User> getCustomerUsers() {
        return userRepository.findByRole(Role.CUSTOMER);
    }
    public List<User> getOrganizationUsers() {
        return userRepository.findByRole(Role.ORGANIZATION);
    }
    public List<ArtistDTO> getArtistDetail() {
        List<User> artistUsers = userRepository.findByRole(Role.ARTIST);
        List<ArtistDTO> artistUserDTOs = new ArrayList<>();

        for (User user : artistUsers) {
            ArtistDTO artistUserDTO = new ArtistDTO();
            artistUserDTO.setId(user.getId());
            artistUserDTO.setFirstname(user.getFirstname());
            artistUserDTO.setLastname(user.getLastname());
            artistUserDTO.setUsername(user.getUsername());
            artistUserDTO.setArtworks(user.getArtworks());
            artistUserDTOs.add(artistUserDTO);
        }
        return artistUserDTOs;
    }
    public List<UserDto> getArtistsWithArtworks() {
        List<Object[]> results = userRepository.findArtistsWithArtworks();

        List<UserDto> artists = new ArrayList<>();
        Map<Long, UserDto> artistMap = new HashMap<>();

        for (Object[] result : results) {
            Long userId = ((Number) result[0]).longValue();
            Long artworkId = ((Number) result[4]).longValue();

            UserDto artist = artistMap.get(userId);
            if (artist == null) {
                artist = new UserDto();
                artist.setFirstname((String) result[1]);
                artist.setLastname((String) result[2]);
                artist.setEmail((String) result[3]);
                artistMap.put(userId, artist);
                artists.add(artist);
            }

            ArtworkDto artwork = new ArtworkDto();
            artwork.setArtworkName((String) result[5]);
            artwork.setArtworkDescription((String) result[6]);
            artwork.setImage((byte[]) result[7]);
            artwork.setPrice((int) result[8]);

            //artist.getArtworks().add(artwork);
        }

        return artists;
    }











    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void initiatePasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        User user = userOptional.get();
        String resetToken = generateResetToken();
        PasswordResetToken passwordResetToken = new PasswordResetToken(resetToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
        String resetLink = "http://localhost:8082/api/users/password/reset?token=" + resetToken;
        String emailContent = "Please click the following link to reset your password: <a href=\"" + resetLink + "\">Reset Password</a>";
        emailService.sendEmail(user.getEmail(), "Password Reset", emailContent);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null || passwordResetToken.isExpired()) {
            throw new UserNotFoundException("Invalid or expired reset token.");
        }
        User user = passwordResetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    private String generateResetToken() {
        StringBuilder resetCode = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int codeLength = 8;
        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            resetCode.append(characters.charAt(index));
        }
        return resetCode.toString();
    }
    public void changePassword(String email, ChangePasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }
        User user = optionalUser.get();
        if (!passwordMatches(user.getPassword(), request.getOldPassword())) {
            throw new IncorrectPasswordException("Incorrect old password.");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    private boolean passwordMatches(String storedPassword, String inputPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(inputPassword, storedPassword);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}