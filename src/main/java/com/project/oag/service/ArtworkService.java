package com.project.oag.service;

import java.util.List;

import com.project.oag.controller.dto.ArtworkDto;
import com.project.oag.entity.Artwork;

public interface ArtworkService {

	List<Artwork> getAllArtworks();

	void saveArtwork(Artwork artwork);

	void deleteArtwork(Long id);

	List<Artwork> getArtworkByCategory(String category);

	List<Artwork> getArtworksByArtistName(String artistName);

	List<Artwork> getArtworksByArtworkName(String artworkName);

	Double getAverageRating(Long artworkId);

	Artwork getArtworkById(Long artworkId);

	ArtworkDto getDtoFromArtwork(Artwork artwork);



	//void saveArtwork(Artwork artwork);


}
