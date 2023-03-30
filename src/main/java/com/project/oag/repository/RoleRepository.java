package com.project.oag.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}