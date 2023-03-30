package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.entity.Artwork;

public interface ArtworkService {

	void saveArtwork(Artwork artwork);

	Optional<Artwork> getArtworkById(Long id);

	List<Artwork> getAllImages();

	//Artwork uploadArtwork(ArtworkDto artworkDto);

	//Artwork updateArtwork(ArtworkDto artworkDto) throws ArtworkNotFoundException;

	//List<Artwork> searchByCategory(String artworkCategory);

	Artwork findById(Long artworkId);

	//Artwork getArtworkById(Long artworkId);

	//Artwork getArtworkById(Long artworkId);

	//Artwork saveArtwork(ArtworkDto artworkDto, MultipartFile multipartFile) throws IOException;
}
