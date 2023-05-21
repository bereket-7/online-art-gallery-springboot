package com.project.oag.service;

import java.util.List;

import com.project.oag.controller.dto.ArtworkDto;
import com.project.oag.entity.Artwork;

public interface ArtworkService {

	List<Artwork> getAllArtworks();

	void saveArtwork(Artwork artwork);

	void deleteArtwork(Long id);

	List<Artwork> getArtworkByCategory(String category);

	Artwork getArtworkById(Long artworkId);

	List<Artwork> getArtworksByArtistId(int artistId);

	List<Artwork> getArtworkByPriceRange(double minPrice, double maxPrice);

	List<Artwork> getPendingArtworks();

	boolean acceptArtwork(Long id);

	boolean rejectArtwork(Long id);

	List<Artwork> getAcceptedArtworks();

	List<Artwork> getRejectedArtworks();

	List<Artwork> getRecentArtworks();

	//void saveArtwork(Artwork artwork);


}
