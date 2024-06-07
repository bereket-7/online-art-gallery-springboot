package com.project.oag.app.repository;

import com.project.oag.app.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<RolePermission, Long> {
}
