package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.controller.dto.ArtworkDto;
import com.project.oag.entity.Artwork;

public interface ArtworkService {

	List<Artwork> getAllArtworks();

	void saveArtwork(Artwork artwork);

	void deleteArtwork(Long id);

	List<Artwork> getArtworkByCategory(String category);

	List<Artwork> getArtworksByArtistId(int artistId);

	List<Artwork> getPendingArtworks();

	boolean acceptArtwork(Long id);

	boolean rejectArtwork(Long id);

	List<Artwork> getAcceptedArtworks();

	List<Artwork> getRejectedArtworks();

	List<Artwork> getRecentArtworks();

	ArtworkDto getDtoFromArtwork(Artwork artwork);

	Optional<Artwork> getArtworkById(Long id);

	List<Artwork> getArtworksByPriceRange(int minPrice, int maxPrice);

}
