package com.project.oag.app.repository;

import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.model.User;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oag.app.model.Artwork;

@Repository
public interface ArtworkRepository  extends JpaRepository<Artwork, Long>{
	@Query("select a from Artwork a where a.artworkCategory = ?1")
	List<Artwork> findByArtworkCategory(String artworkCategory);

	@Query("select a from Artwork a where a.artistId = ?1")
	List<Artwork> findByArtistId(Long artistId);

	@Query("select a from Artwork a where a.status = ?1")
	List<Artwork> findByStatus(ArtworkStatus status);

	List<Artwork> findByPriceBetween(int minPrice, int maxPrice);

	@Query("select a from Artwork a order by a.creationDate DESC")
	List<Artwork> findRecentArtworks();

	@Query("SELECT a.artworkCategory, COUNT(a) FROM Artwork a GROUP BY a.artworkCategory")
	List<Object[]> countByCategory();
	@Query("SELECT a FROM Artwork a WHERE a.artworkCategory LIKE %:keyword% " +
			"OR a.artworkName LIKE %:keyword% OR a.price LIKE %:keyword%")
	List<Artwork> searchArtwork(@Param("keyword") String keyword, Pageable pageable);
	@Query("SELECT a.artworkCategory FROM Artwork a WHERE a.artworkCategory LIKE %:keyword% " +
			"OR a.artworkName LIKE %:keyword% OR a.price LIKE %:keyword%")
	List<String> getAutocompleteResults(@Param("keyword") String keyword);

	@Query("SELECT a FROM Artwork a JOIN a.ratings r GROUP BY a.id ORDER BY AVG(r.rating) DESC")
	List<Artwork> findAllByOrderByAverageRatingDesc();
	List<Artwork> findAllByOrderByPriceAsc();
	List<Artwork> findAllByOrderByPriceDesc();
    List<Artwork> findByArtist(User artist);
}
