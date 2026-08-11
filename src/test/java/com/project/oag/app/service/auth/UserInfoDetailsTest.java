package com.project.oag.app.service.auth;

import com.project.oag.app.entity.Permission;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.UserRole;
import com.project.oag.app.service.auth.UserInfoDetails;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserInfoDetailsTest {

    @Test
    void includesRoleAndPermissionsInAuthorities() {
        Permission cart = new Permission();
        cart.setPermissionName("USER_MODIFY_CART");

        UserRole role = new UserRole();
        role.setRoleName("ROLE_CUSTOMER");
        role.setPermissions(Set.of(cart));

        User user = new User();
        user.setEmail("customer@test.com");
        user.setPassword("pwd");
        user.setVerified(true);
        user.setUserRole(role);

        UserInfoDetails details = new UserInfoDetails(user);

        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER")));
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER_MODIFY_CART")));
    }
}
