package com.project.oag.app.repository;

import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.entity.Artwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long>, JpaSpecificationExecutor<Artwork> {
    @Query("select a from Artwork a where a.artworkCategory = ?1")
    List<Artwork> findByArtworkCategory(String artworkCategory);

    @Query("select a from Artwork a where a.user.id = ?1")
    List<Artwork> findByArtistId(Long artistId);

    @Query("select a from Artwork a where a.status = ?1")
    List<Artwork> findByStatus(ArtworkStatus status);

    @Query("""
        SELECT NEW com.project.oag.app.dto.ArtworkResponseDto(
            a.artworkName,
            a.artworkDescription,
            a.artworkCategory,
            a.price,
            a.size,
            a.imageUrls,
            a.ratings,
            a.user.id)
        FROM Artwork a
        ORDER BY a.creationDate DESC
    """)
    Page<ArtworkResponseDto> findRecentArtworks(Pageable pageable);

    @Query("SELECT a.artworkCategory, COUNT(a) FROM Artwork a GROUP BY a.artworkCategory")
    List<Object[]> countByCategory();

}
