package com.project.oag.utils;

import com.project.oag.config.security.captcha.ErrorCode;

import org.apache.commons.lang3.ObjectUtils;

public class RecaptchaUtils {
    public static boolean hasClientError(ErrorCode[] errors) {
        if (ObjectUtils.isEmpty(errors)) {
            return false;
        }
        for (ErrorCode error : errors) {
            switch (error) {
                case INVALID_RESPONSE:
                case MISSING_RESPONSE:
                    return true;
            }
        }
        return false;
    }
}
