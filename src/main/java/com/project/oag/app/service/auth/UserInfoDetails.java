package com.project.oag.app.service.auth;

import com.project.oag.app.entity.Permission;
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
    private final boolean accountNonLocked;
    private final boolean enabled;
    private final List<GrantedAuthority> authorityList = new ArrayList<>();

    public UserInfoDetails(User user) {
        username = user.getEmail();
        password = user.getPassword();
        accountNonLocked = !Boolean.TRUE.equals(user.getLocked())
                && (user.getBlockedUntil() == null || user.getBlockedUntil().before(new java.sql.Timestamp(System.currentTimeMillis())));
        enabled = user.isVerified();
        UserRole userRole = user.getUserRole();
        if (userRole != null) {
            authorityList.add(new SimpleGrantedAuthority(userRole.getRoleName()));
            if (userRole.getPermissions() != null) {
                for (Permission permission : userRole.getPermissions()) {
                    authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName()));
                }
            }
        }
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
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
