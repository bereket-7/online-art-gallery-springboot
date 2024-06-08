package com.project.oag.app.service;

import com.project.oag.app.dto.*;
import com.project.oag.app.entity.Otp;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.OtpRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.common.service.impl.EmailService;
import com.project.oag.config.properties.EmailConfig;
import com.project.oag.config.properties.OtpConfig;
import com.project.oag.exceptions.GeneralException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.NotificationUtils.generateBodyForEmail;
import static com.project.oag.utils.NotificationUtils.getSubjectForEmail;
import static com.project.oag.utils.OtpUtils.customHash;
import static com.project.oag.utils.OtpUtils.generateOtp;
import static com.project.oag.utils.TimeUtils.*;
import static com.project.oag.utils.Utils.prepareResponse;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.OK;

@Service
@Slf4j
public class OtpService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmailConfig emailConfig;
    private final OtpConfig otpConfig;
    private final OtpRepository otpRepository;


    public OtpService(EmailService emailService, UserRepository userRepository, EmailConfig emailConfig, OtpConfig otpConfig, OtpRepository otpRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.emailConfig = emailConfig;
        this.otpConfig = otpConfig;
        this.otpRepository = otpRepository;
    }

    private static NotificationInfoDto getNotificationInfo(OtpRequestDto otpRequestDto, String otpCode) {
        return NotificationInfoDto.builder()
                .name(otpRequestDto.getName())
                .otp(otpCode)
                .notificationType(NotificationType.LOGIN_OTP)
                .build();
    }

    public boolean sendOtpNotification(final OtpRequestDto otpRequestDto) {
        final String otpCode = generateOtp(otpConfig.length(), otpRequestDto.getOtpType());
        val otpRecord = new Otp();
        otpRecord.setUserId(otpRequestDto.getUserId());
        otpRecord.setOtpCode(customHash(otpCode, otpRequestDto.getUserId(), otpRequestDto.getNotificationType(), otpConfig.salt()));
        otpRecord.setNotificationType(otpRequestDto.getNotificationType());
        otpRecord.setOtpCreationTime(getTimestampNow());
        otpRecord.setOtpExpiryTime(getTimestampAfterGivenMinutes(otpConfig.expireAfter()));
        log.info("###### WILL BE REMOVED ##### {} ", otpCode);
        otpRepository.save(otpRecord);
        switch (otpRequestDto.getNotificationChannel()) {
            case EMAIL -> {
                return emailService.sendNotification(NotificationDto.builder()
                        .sendTo(otpRequestDto.getAddress())
                        .subject(getSubjectForEmail(otpRequestDto.getNotificationType(), emailConfig))
                        .body(generateBodyForEmail(getNotificationInfo(otpRequestDto, otpCode), emailConfig))
                        .build());
            }
            case SMS -> {
                log.warn(LOG_PREFIX, "SMS not integrated yet.");
                throw new GeneralException("SMS not integrated yet.");
            }
            default -> {
                log.error(LOG_PREFIX, "Invalid notification method", otpRequestDto.getNotificationChannel());
                throw new IllegalStateException("Invalid notification method: " + otpRequestDto.getNotificationChannel());
            }
        }
    }
    public ResponseEntity<GenericResponse> verifyOtpCode(VerifyOtpRequestDTO verifyOtpRequestDTO, NotificationType notificationType) {
        val userId = getUserId(verifyOtpRequestDTO.getUsername());
        if (userId == null) {
            return prepareResponse(HttpStatus.UNAUTHORIZED, "Failed to verify, User not found", emptyList());
        }
        val expectedOtpHash = customHash(verifyOtpRequestDTO.getOtp(), userId, notificationType, otpConfig.salt());
        val otpResult = otpRepository.findByOtpCode(expectedOtpHash);
        return otpResult.map(this::checkOtpExpiration)
                .orElse(prepareResponse(HttpStatus.UNAUTHORIZED, "Failed to verify, Otp code not found", emptyList()));
    }

    private ResponseEntity<GenericResponse> checkOtpExpiration(Otp otp) {
        if (otp.getOtpExpiryTime()
                .after(getTimestampNow())) {
            log.info(LOG_PREFIX, "Otp check success", "");
            otpRepository.delete(otp);
            log.info(LOG_PREFIX, "Otp deleted", "");
            return prepareResponse(OK, "Success", emptyList());
        } else {
            return prepareResponse(HttpStatus.FORBIDDEN, "Failed to verify, Otp expired", emptyList());
        }
    }

    private Long getUserId(final String username) {
        return userRepository.findByEmailIgnoreCase(username)
                .map(User::getId)
                .orElse(null);
    }

    public boolean hasActiveOtp(final long userId) {
        return otpRepository.findByUserIdAndOtpExpiryTimeAfterOrderByOtpExpiryTimeDesc(userId, getTimestampNow())
                .stream().findFirst()
                .map(otp -> getTimestampAfterGivenMinutesGivenTimestamp(otpConfig.allowResendAfter(), otp.getOtpCreationTime())
                        .after(getTimestampNow()))
                .orElse(false);
    }

    public boolean canResendOtp(final long userId) {
        return otpRepository.findByUserIdAndOtpExpiryTimeAfterOrderByOtpExpiryTimeDesc(userId, getTimestampNow())
                .stream().findFirst()
                .map(otp -> getTimestampAfterGivenMinutesGivenTimestamp(otpConfig.allowResendAfter(), otp.getOtpCreationTime())
                        .after(getTimestampNow()))
                .orElse(false);
    }

    public GenericResponse getOtpSettings() {
        return GenericResponse.builder()
                .content(OtpResponseDto.builder()
                        .expireAfter(otpConfig.expireAfter())
                        .allowResendAfter(otpConfig.allowResendAfter())
                        .build())
                .build();
    }
}