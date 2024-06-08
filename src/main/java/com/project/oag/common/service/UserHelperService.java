package com.project.oag.common.service;

import com.project.oag.app.dto.auth.UserDto;
import com.project.oag.app.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserHelperService {
    Long getUserId(HttpServletRequest request);
    UserDto getUserByUsername(String email);
    User getUserEntity(HttpServletRequest request);
    Long getUserIdFromRequest(HttpServletRequest request);
    Boolean isAdminFromRequest(HttpServletRequest request);
}
