package com.project.oag.app.service;

import com.project.oag.app.dto.ArtistDTO;
import com.project.oag.app.dto.GenericResponsePageable;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.PasswordResetTokenRepository;
import com.project.oag.app.repository.UserInfoByRole;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.project.oag.utils.PageableUtils.preparePageInfo;
import static com.project.oag.utils.RequestUtils.getIpAddressFromHeader;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;
import static com.project.oag.utils.Utils.prepareResponseWithPageable;

@Service
@Configuration
public class CustomUserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public CustomUserDetailsService(UserRepository userRepository, ModelMapper modelMapper, ConfirmationTokenService confirmationTokenService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.confirmationTokenService = confirmationTokenService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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
    public Long getTotalArtistUsers(String roleName) {
        return userRepository.countUserByUserRole(roleName);
    }
    public ResponseEntity<GenericResponsePageable> getUsersByRole(String roleName, Pageable pageable) {
        try {
            val response = userRepository.findUserByRoleName(roleName,pageable);
            return prepareResponseWithPageable(HttpStatus.OK, "Fetched Users by this role {} " + roleName, response.getContent(),preparePageInfo(response));
        } catch (Exception e) {
            throw new GeneralException("Error while retrieving users by role");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    private Long getUserId(HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }

}