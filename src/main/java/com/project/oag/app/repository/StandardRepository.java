package com.project.oag.app.repository;

import com.project.oag.app.dto.StandardType;
import com.project.oag.app.model.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRepository extends JpaRepository<Standard, Long> {
    List<Standard> findByStandardType(StandardType standardType);
}
