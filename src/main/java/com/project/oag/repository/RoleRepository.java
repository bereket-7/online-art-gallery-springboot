package com.project.oag.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Role;
import com.project.oag.entity.Role.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    void delete(Role role);

    Role findByName(RoleType roleType);
}
