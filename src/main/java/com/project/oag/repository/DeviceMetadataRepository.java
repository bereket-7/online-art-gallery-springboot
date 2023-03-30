package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.DeviceMetadata;

public interface DeviceMetadataRepository extends JpaRepository<DeviceMetadata, Long> {

    List<DeviceMetadata> findByUserId(Long userId);
}