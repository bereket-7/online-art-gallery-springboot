package com.project.oag.standard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRepository extends JpaRepository<Standard, Long> {
    List<Standard> findByStandardType(String standardType);
}
