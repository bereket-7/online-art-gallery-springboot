package com.project.oag.utils;


import com.project.oag.app.dto.NotificationInfoDto;
import com.project.oag.app.dto.NotificationType;
import com.project.oag.config.properties.EmailConfig;
import com.project.oag.exceptions.GeneralException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationUtils {
    private NotificationUtils() {

    }

    public static String getSubjectForEmail(NotificationType notificationType, EmailConfig emailConfig) {
        try {
            switch (notificationType) {
                case ACCOUNT_CREATED -> {
                    return emailConfig.accountVerifiedSubject();
                }
                case LOGIN_OTP, SIGNUP_OTP -> {
                    return emailConfig.verifyEmailSubject();
                }
                case FORGOT_PASSWORD_REQUEST -> {
                    return emailConfig.passwordForgotSubject();
                }
                case RESET_PASSWORD_SUCCESS -> {
                    return emailConfig.resetSuccessSubject();
                }
                case EMAIL_UPDATED -> {
                    return emailConfig.emailChangeInitiate();
                }
                case PHONE_NUMBER_UPDATED -> {
                    return emailConfig.phoneChangeInitiate();
                }
                default -> {
                    log.info("Invalid NotificationType while preparing body content");
                    throw new GeneralException("Invalid NotificationType while preparing body content");
                }
            }
        } catch (Exception e) {
            log.info("Exception while preparing body content", e);
            throw new GeneralException("Exception while preparing body content" + e.getMessage());
        }
    }


    public static String generateBodyForEmail(NotificationInfoDto infoDto, EmailConfig emailConfig) {
        try {
            switch (infoDto.getNotificationType()) {
                case ACCOUNT_CREATED -> {
                    return emailConfig.accountVerifiedContent();
                }
                case LOGIN_OTP, SIGNUP_OTP -> {
                    return emailConfig.verifyEmailContent()
                            .replace("{0}", infoDto.getName())
                            .replace("{1}", emailConfig.appName())
                            .replace("{2}", infoDto.getOtp());
                }
                case FORGOT_PASSWORD_REQUEST -> {
                    return emailConfig.passwordForgotContent()
                            .replace("{0}", infoDto.getName())
                            .replace("{1}", infoDto.getOtp());
                }
                case RESET_PASSWORD_SUCCESS -> {
                    return emailConfig.resetSuccessContent()
                            .replace("{0}", infoDto.getName());
                }
                case PHONE_NUMBER_UPDATED -> {
                    return emailConfig.phoneChangeInitiate()
                            .replace("{0}", infoDto.getName())
                            .replace("{1}", infoDto.getOtp());
                }
                case EMAIL_UPDATED -> {
                    return emailConfig.emailChangeInitiate()
                            .replace("{0}", infoDto.getName())
                            .replace("{1}", infoDto.getOtp());
                }
                default -> {
                    log.info("Invalid NotificationType while preparing body content");
                    throw new GeneralException("Invalid NotificationType while preparing body content");
                }
            }
        } catch (Exception e) {
            log.info("Exception while preparing body content", e);
            throw new GeneralException("Exception while preparing body content" + e.getMessage());
        }
    }
}
