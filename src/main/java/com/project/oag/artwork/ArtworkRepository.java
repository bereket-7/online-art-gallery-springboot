package com.project.oag.artwork;

import com.project.oag.user.User;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oag.artwork.Artwork;

@Repository
public interface ArtworkRepository  extends JpaRepository<Artwork, Long>{
	 List<Artwork> findByArtworkCategory(String artworkCategory);
	List<Artwork> findByPriceBetween(int minPrice, int maxPrice);
	 List<Artwork> findByStatus(String status);
	  List<Artwork> findAllByOrderByCreateDateDesc();
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
