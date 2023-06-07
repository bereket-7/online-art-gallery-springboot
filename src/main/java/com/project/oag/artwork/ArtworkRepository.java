package com.project.oag.artwork;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oag.artwork.Artwork;

@Repository
public interface ArtworkRepository  extends JpaRepository<Artwork, Long>{
	 List<Artwork> findByArtworkCategory(String artworkCategory);
	 List<Artwork> findByArtistId(int artistId);
	 List<Artwork> findByPriceBetween(double minPrice, double maxPrice);
	 List<Artwork> findByStatus(String status);
	  List<Artwork> findAllByOrderByCreateDateDesc();
	@Query("SELECT a.artworkCategory, COUNT(a) FROM Artwork a GROUP BY a.artworkCategory")
	List<Object[]> countByCategory();

	@Query("SELECT a FROM Artwork a WHERE a.artworkCategory LIKE %:keyword% " +
			"OR a.artworkName LIKE %:keyword% OR a.price LIKE %:keyword%")
	List<Artwork> searchArtwork(@Param("keyword") String keyword);

	@Query("SELECT a.artworkCategory FROM Artwork a WHERE a.artworkCategory LIKE %:keyword% " +
			"OR a.artworkName LIKE %:keyword% OR a.price LIKE %:keyword%")
	List<String> getAutocompleteResults(@Param("keyword") String keyword);
}
