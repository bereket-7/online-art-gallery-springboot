package com.project.oag.app.repository;

import com.project.oag.app.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByAssignableTrueAndAdmin(boolean admin);

    List<RolePermission> findByPermissionIdInAndAssignableTrueAndAdmin(Collection<Long> permissionIds, boolean admin);
}
