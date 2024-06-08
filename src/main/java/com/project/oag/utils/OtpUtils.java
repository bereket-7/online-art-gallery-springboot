package com.project.oag.utils;

import com.project.oag.app.dto.NotificationType;
import com.project.oag.app.dto.OtpCodeTypeDto;
import lombok.val;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;

public class OtpUtils {
    public static final String ALL = "all";
    public static SecureRandom secureRandom = new SecureRandom();

    private OtpUtils() {

    }

    public static String generateOtp(int length, OtpCodeTypeDto otpType) {
        if (otpType.toString().equalsIgnoreCase(ALL)) {
            return RandomString.make(length);
        } else {
            return String.valueOf(secureRandom.nextInt(length));
        }
    }


    public static String customHash(final CharSequence otp, final long userId, final NotificationType notificationType, final String salt) {
        val otpWithUserInfo = otp.toString().concat(String.valueOf(userId))
                .concat(notificationType.toString());
        return customHashWithSalt(otpWithUserInfo, salt);
    }

    public static String customHashWithSalt(CharSequence rawPassword, final String salt) {
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }
}
