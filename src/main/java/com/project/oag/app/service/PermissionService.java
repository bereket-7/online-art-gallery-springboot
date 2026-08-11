package com.project.oag.app.service;

import com.project.oag.app.entity.Permission;
import com.project.oag.app.entity.UserRole;
import com.project.oag.app.repository.PermissionRepository;
import com.project.oag.app.repository.RoleRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public List<String> getPermissionNamesForRole(UserRole role) {
        if (role == null || role.getPermissions() == null) {
            return List.of();
        }
        return role.getPermissions().stream()
                .map(Permission::getPermissionName)
                .toList();
    }

    @Transactional
    public UserRole assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        UserRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId));
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.getPermissions().clear();
        role.getPermissions().addAll(permissions);
        return roleRepository.save(role);
    }
}
