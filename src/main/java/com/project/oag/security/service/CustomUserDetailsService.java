package com.project.oag.security.service;

import com.project.oag.app.dto.ArtistDTO;
import com.project.oag.app.dto.ChangePasswordRequest;
import com.project.oag.app.dto.Role;
import com.project.oag.app.dto.UserRequestDto;
import com.project.oag.app.model.ConfirmationToken;
import com.project.oag.app.model.PasswordResetToken;
import com.project.oag.app.model.User;
import com.project.oag.app.repository.PasswordResetTokenRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.ConfirmationTokenService;
import com.project.oag.app.service.EmailService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.IncorrectPasswordException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Configuration
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public CustomUserDetailsService(UserRepository userRepository, ModelMapper modelMapper, ConfirmationTokenService confirmationTokenService, EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new Exception("user Not found "));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<GenericResponse> signUpUser(UserRequestDto userRequestDto) {
        try {
            userRepository.findByEmailIgnoreCase(userRequestDto.getEmail())
                    .orElseThrow(() -> new GeneralException("This Email already registered"));
            val savedUser = modelMapper.map(userRequestDto, User.class);
            userRepository.save(savedUser);
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), savedUser);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            return prepareResponse(HttpStatus.OK, "User sign up successfully", token);
        } catch (Exception e) {
            throw new GeneralException("Failed to save user");
        }
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public ResponseEntity<GenericResponse> uploadProfilePhoto(HttpServletRequest request, String photoUrl) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User " + email + " not found"));
        try {
            user.setImage(photoUrl);
            userRepository.save(user);
            return prepareResponse(HttpStatus.OK, "User profile photo updated successfully", user);
        } catch (Exception e) {
            throw new GeneralException("Failed to upload profile photo");
        }
    }

    public ResponseEntity<GenericResponse> getProfilePhoto(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        try {
            User user = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UserNotFoundException("User " + email + " not found"));
            val profilePhoto = user.getImage();
            return prepareResponse(HttpStatus.OK, "User profile photo retrieved successfully", profilePhoto);
        } catch (Exception e) {
            throw new GeneralException("Failed to fetch profile photo");
        }
    }

    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void initiatePasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
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
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
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

    private Long getUserId(HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }

}