package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Artwork;

public interface ArtworkRepository  extends JpaRepository<Artwork, Long>{
	//List<Artwork> findByArtistIdAndStatus(Long id, String status);
	Artwork findByStatus(String status);
	Artwork findById(String artworkName);
	//List<Artwork> findByCategory(String artworkCategory);
	//Optional<Artwork> save(Optional<Artwork> artwork);
	List<Artwork> findByIdAndStatus(Long artistName, String string);
}
