package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.entity.Artwork;

public interface ArtworkService {

	void saveArtwork(Artwork artwork);

	Optional<Artwork> getArtworkById(Long id);

	List<Artwork> getAllArtworks();
}
