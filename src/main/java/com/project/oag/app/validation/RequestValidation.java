package com.project.oag.app.validation;

import com.project.oag.app.dto.auth.PasswordResetRequest;
import com.project.oag.app.dto.auth.RegisterUserRequestDto;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.spi.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestValidation {
    private RequestValidation() {

    }

    public static boolean validateAddPermissionsToRoleRequest(String roleId, List<Long> permissionId) {
        return !StringUtils.isEmpty(roleId) && !ObjectUtils.isEmpty(permissionId);
    }

    public static void validateRegisterUserRequest(RegisterUserRequestDto user) {
        List<ErrorMessage> messages = new ArrayList<>();
        if (ObjectUtils.isEmpty(user))
            messages.add(new ErrorMessage("Request object is empty. "));
        else {
            if (StringUtils.isEmpty(user.getEmail())) {
                messages.add(new ErrorMessage("Email is empty. "));
            }
            if (StringUtils.isEmpty(user.getLastName())) {
                messages.add(new ErrorMessage("First name  is empty. "));
            }
            if (StringUtils.isEmpty(user.getFirstName())) {
                messages.add(new ErrorMessage("last name is empty. "));
            }
            if (StringUtils.isEmpty(user.getPassword())) {
                messages.add(new ErrorMessage("password is empty. "));
            } else {
                if (!user.isPasswordMatch()) {
                    messages.add(new ErrorMessage("Password and Confirm password fields should be the same"));
                }
            }
            if (StringUtils.isEmpty(user.getPhone())) {
                messages.add(new ErrorMessage("phone is empty is empty. "));
            }
        }
        if (ObjectUtils.isNotEmpty(messages)) {
            throw new ValidationException(getMessagesAsString(messages));
        }
    }

    private static String getMessagesAsString(List<ErrorMessage> messages) {
        return messages.stream()
                .map(ErrorMessage::getMessage)
                .collect(Collectors.joining(","));
    }

    public static void validateResetPasswordRequest(PasswordResetRequest passwordResetRequest) {
        List<ErrorMessage> messages = new ArrayList<>();
        if (ObjectUtils.isEmpty(passwordResetRequest)
                || StringUtils.isEmpty(passwordResetRequest.otp())
                || StringUtils.isEmpty(passwordResetRequest.email())
                || StringUtils.isEmpty(passwordResetRequest.password())
                || StringUtils.isEmpty(passwordResetRequest.confirmPassword()))
            messages.add(new ErrorMessage("Empty field found please make sure all the fields are properly filled"));
        if (StringUtils.isEmpty(passwordResetRequest.password())) {
            messages.add(new ErrorMessage("password is empty. "));
        } else {
            if (!passwordResetRequest.isPasswordMatch())
                messages.add(new ErrorMessage("Password and Confirm password fields should be the same"));
        }
        if (ObjectUtils.isNotEmpty(messages)) {
            throw new ValidationException(getMessagesAsString(messages));
        }
    }
}
