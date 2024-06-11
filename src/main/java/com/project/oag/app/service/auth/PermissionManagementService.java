package com.project.oag.app.service.auth;


import com.project.oag.app.dto.auth.RolePermissionDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.PermissionRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class PermissionManagementService {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public PermissionManagementService(PermissionRepository permissionRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<GenericResponse> fetchAllPermissions(HttpServletRequest request) {
        try {
            val permissions = permissionRepository.findByAssignableTrueAndAdmin(checkUserAdminRoleFlag(request));
            log.info(LOG_PREFIX, "Successfully fetched All Permissions ", "");
            return ResponseEntity.ok(new GenericResponse(HttpStatus.CREATED.value()
                    , "Successfully fetched All Permissions ", permissions.stream().map((element) -> modelMapper.map(element, RolePermissionDto.class)).collect(Collectors.toList())));
        } catch (Exception e) {
            log.info(LOG_PREFIX, "Failed fetching permissions", e.getMessage());
            return ResponseEntity.ok(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value()
                    , "Failed fetching permissions " + e.getLocalizedMessage(), Collections.emptyList()));
        }
    }

    private boolean checkUserAdminRoleFlag(HttpServletRequest request) {
        return getCurrentUser(request).getUserRole()
                .isAdmin();
    }

    private User getCurrentUser(HttpServletRequest request) {
        return userRepository.findByEmailIgnoreCase(getLoggedInUserName(request))
                .orElseThrow(() -> new UserNotFoundException("Unable to get user information"));
    }

}
