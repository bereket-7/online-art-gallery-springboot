package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Standard;

@Repository
public interface StandardRepository extends JpaRepository<Standard, Long> {
}
