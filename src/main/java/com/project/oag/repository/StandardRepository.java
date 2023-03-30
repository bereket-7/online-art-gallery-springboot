package com.project.oag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer>{
	void deleteById(Long id);

	Optional<Standard> findById(Long id);

}
