package com.project.oag.app.service.auth;

import com.project.oag.app.repository.RoleRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.service.UserHelperService;
import com.project.oag.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleManagementService {
    public static final String FAILED_ADDING_ROLES_TO_USER = "Failed adding roles to user , ";
    public static final String NO_ROLE_FOUND_WITH_ROLE_ID = "No role found with roleId, ";
    public static final String SUCCESSFULLY_UPDATED_ROLE = "Successfully updated role ";
    public static final String SUCCESSFULLY_FETCHED_ALL_ROLES = "Successfully fetched all roles";
    public static final String UNABLE_TO_DELETE = "Unable to delete, the specified role has the following associated permissions";
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final UserHelperService userHelperService;

    public RoleManagementService(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository, ModelMapperUtils modelMapperUtils, UserHelperService userHelperService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userHelperService = userHelperService;
    }

}
