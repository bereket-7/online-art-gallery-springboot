package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Artwork;

@Repository
public interface ArtworkRepository  extends JpaRepository<Artwork, Long>{
	 List<Artwork> findByArtworkCategory(String artworkCategory);
	 
	 List<Artwork> findByArtistId(int artistId);
	 
	 List<Artwork> findByPriceBetween(double minPrice, double maxPrice);
	 
	 List<Artwork> findByStatus(String status);

	  List<Artwork> findAllByOrderByCreateDateDesc();
}
