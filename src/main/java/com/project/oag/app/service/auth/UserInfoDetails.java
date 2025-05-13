package com.project.oag.app.service.auth;

import com.project.oag.app.entity.User;
import com.project.oag.app.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorityList = new ArrayList<>();

    /**
     * Prepare user roles, permissions and add theme to authorityList
     *
     * @param user
     */
    public UserInfoDetails(User user) {
        username = user.getEmail();
        password = user.getPassword();
        UserRole userRole = user.getUserRole();
        authorityList.add(new SimpleGrantedAuthority(userRole.getRoleName()));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
