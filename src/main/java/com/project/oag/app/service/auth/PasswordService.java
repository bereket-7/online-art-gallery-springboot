package com.project.oag.app.service.auth;

import com.project.oag.app.dto.*;
import com.project.oag.app.dto.auth.PasswordForgotRequest;
import com.project.oag.app.dto.auth.PasswordResetRequest;
import com.project.oag.app.dto.auth.UserDto;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.OtpService;
import com.project.oag.common.GenericResponse;
import com.project.oag.common.service.impl.EmailService;
import com.project.oag.config.properties.EmailConfig;
import com.project.oag.config.properties.PasswordConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.project.oag.app.validation.RequestValidation.validateResetPasswordRequest;
import static com.project.oag.common.AppConstants.*;
import static com.project.oag.utils.NotificationUtils.generateBodyForEmail;
import static com.project.oag.utils.Utils.prepareResponse;
import static java.util.Collections.emptyList;

@Service
@Slf4j
public class PasswordService {
        private final OtpService otpService;
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;
        private final EmailService emailService;
        private final EmailConfig emailConfig;
        private final PasswordConfig passwordConfig;
        private final PasswordEncoder passwordEncoder;

        public PasswordService(OtpService otpService, UserRepository userRepository, ModelMapper modelMapper, EmailService emailService, EmailConfig emailConfig, PasswordConfig passwordConfig, PasswordEncoder passwordEncoder) {
            this.otpService = otpService;
            this.userRepository = userRepository;
            this.modelMapper = modelMapper;
            this.emailService = emailService;
            this.emailConfig = emailConfig;
            this.passwordConfig = passwordConfig;
            this.passwordEncoder = passwordEncoder;
        }

        private static String getFullName(UserDto user) {
            return user.getFirstName().concat(SPACE).concat(user.getLastName());
        }

        public ResponseEntity<GenericResponse> forgotPassword(PasswordForgotRequest passwordForgotRequest) {
            log.info(LOG_PREFIX, "Forgot Password verification process for username", passwordForgotRequest.email());
            val user = getUserByUsername(passwordForgotRequest.email());
            if (user == null) {
                log.info(LOG_PREFIX, "Forgot Password verification process failed, user not found for username ", passwordForgotRequest.email());
                return prepareResponse(HttpStatus.OK, USER_NOT_FOUND, emptyList());
            }

            if (!user.isVerified()) {
                log.info(LOG_PREFIX, "Account not verified ", passwordForgotRequest.email());
                return prepareResponse(HttpStatus.UNAUTHORIZED, "Account not verified", emptyList());
            }

            if (passwordForgotRequest.resendOtp()) {
                if (otpService.canResendOtp(user.getId())) {
                    return prepareResponse(HttpStatus.TOO_EARLY, "Not allowed to make new request now, " +
                            "Please wait for some time and try again Already have active Otp", emptyList());
                }
                log.info(LOG_PREFIX, "Resend verifying OTP for username ", passwordForgotRequest.email());
                return sendOtp(passwordForgotRequest, user.getId(), getFullName(user), NotificationType.FORGOT_PASSWORD_REQUEST);
            }

            if (otpService.hasActiveOtp(user.getId())) {
                return prepareResponse(HttpStatus.CONFLICT, "Already have active Otp", emptyList());
            }

            val fullName = getFullName(user);
            return sendOtp(passwordForgotRequest, user.getId(), fullName, NotificationType.FORGOT_PASSWORD_REQUEST);
        }

        private ResponseEntity<GenericResponse> sendOtp(PasswordForgotRequest passwordForgotRequest, long userId, String fullName, NotificationType notificationType) {
            boolean isOtpSent = otpService.sendOtpNotification(
                    OtpRequestDto.builder()
                            .name(fullName)
                            .userId(userId)
                            .address(passwordForgotRequest.email())
                            .otpType(OtpCodeTypeDto.ALL)
                            .notificationType(notificationType)
                            .notificationChannel(NotificationChannel.EMAIL)
                            .build());
            if (isOtpSent) {
                log.info(LOG_PREFIX, "Sent OTP to user address for username", passwordForgotRequest.email());
                return prepareResponse(HttpStatus.OK, OTP_SENT, emptyList());
            } else {
                log.info(LOG_PREFIX, "Failed to Send OTP for username", passwordForgotRequest.email());
                return prepareResponse(HttpStatus.INTERNAL_SERVER_ERROR, FAILED_TO_SEND, emptyList());
            }
        }
  

        public ResponseEntity<GenericResponse> resetPassword(PasswordResetRequest passwordResetRequest) {
            validateResetPasswordRequest(passwordResetRequest);
            log.info(LOG_PREFIX, "Started verifying OTP for username ", passwordResetRequest.email());
            val verifyOtpResponse = otpService.verifyOtpCode(VerifyOtpRequestDTO.builder()
                    .otp(passwordResetRequest.otp())
                    .username(passwordResetRequest.email())
                    .build(), NotificationType.FORGOT_PASSWORD_REQUEST);
            if (!verifyOtpResponse.getStatusCode().is2xxSuccessful()) {
                log.info(LOG_PREFIX, "Failed to verify the OTP for username ", passwordResetRequest.email());
                return verifyOtpResponse;
            }

            val user = getUserByUsername(passwordResetRequest.email());
            //update user to verified
            log.info(LOG_PREFIX, "Updating user password", passwordResetRequest.email());
            userRepository.updatePasswordByEmailIgnoreCase(passwordEncoder.encode(passwordResetRequest.password()), passwordResetRequest.email());
            log.info(LOG_PREFIX, "Successfully updated user password", passwordResetRequest.email());
            emailService.sendNotification(NotificationDto.builder()
                    .sendTo(passwordResetRequest.email())
                    .subject(emailConfig.resetSuccessSubject())
                    .body(generateBodyForEmail(NotificationInfoDto.builder()
                            .name(getFullName(user))
                            .notificationType(NotificationType.RESET_PASSWORD_SUCCESS)
                            .build(), emailConfig))
                    .build());
            return prepareResponse(HttpStatus.OK, "You have successfully reset your password", emptyList());
        }

        private UserDto getUserByUsername(String email) {
            return userRepository.findByEmailIgnoreCase(email)
                    .stream().findFirst().map((element) -> modelMapper.map(element, UserDto.class))
                    .orElse(null);
        }
    }