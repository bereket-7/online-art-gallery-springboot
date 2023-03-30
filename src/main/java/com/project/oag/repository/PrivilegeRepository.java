package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);

}