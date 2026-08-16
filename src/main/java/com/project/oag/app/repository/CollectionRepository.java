package com.project.oag.app.repository;

import com.project.oag.app.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByFeaturedTrue();

    Optional<Collection> findBySlug(String slug);
}
