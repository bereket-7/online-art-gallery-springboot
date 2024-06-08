package com.project.oag.common.service.impl;

import com.project.oag.app.dto.auth.UserDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.service.UserHelperService;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.project.oag.common.AppConstants.IS_ADMIN;
import static com.project.oag.common.AppConstants.USER_ID;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class UserHelperServiceImpl implements UserHelperService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserHelperServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long getUserId(final HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    @Override
    public UserDto getUserByUsername(final String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .stream().findFirst().map((element) -> modelMapper.map(element, UserDto.class))
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }

    @Override
    public User getUserEntity(final HttpServletRequest request) {
        final String loggedInUserName = getLoggedInUserName(request);
        return userRepository.findByEmailIgnoreCase(loggedInUserName)
                .stream().findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + loggedInUserName));
    }

    @Override
    public Long getUserIdFromRequest(HttpServletRequest request) {
        return (Long) request.getAttribute(USER_ID);
    }

    @Override
    public Boolean isAdminFromRequest(HttpServletRequest request) {
        return (Boolean) request.getAttribute(IS_ADMIN);
    }

}
