package com.project.oag.app.repository;

import com.project.oag.app.entity.ArtworkView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkViewRepository extends JpaRepository<ArtworkView, Long> {
    @Query("""
            SELECT av.artwork.id, COUNT(av)
            FROM ArtworkView av
            GROUP BY av.artwork.id
            ORDER BY COUNT(av) DESC
            """)
    List<Object[]> findTrendingArtworkIds();
}
