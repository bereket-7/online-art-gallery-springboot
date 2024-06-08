package com.project.oag.app.service;

import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.auth.UserInfoDetails;
import com.project.oag.common.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserInfoUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        log.info(AppConstants.LOG_PREFIX, "Fetching user with username: ", email);
        var userInfo = repository.findByEmailIgnoreCase(email);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> {
                    log.error(AppConstants.LOG_PREFIX, "User not found for give username: ", email);
                    return new UsernameNotFoundException("User not found for given email: " + email);
                });
    }
}
