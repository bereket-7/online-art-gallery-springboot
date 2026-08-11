package com.project.oag.app.repository;

import com.project.oag.app.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
    List<Permission> findByPermissionNameIn(List<String> permissionNames);
}
