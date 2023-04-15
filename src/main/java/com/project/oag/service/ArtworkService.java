package com.project.oag.service;

import java.util.List;

import com.project.oag.entity.Artwork;
import com.project.oag.entity.User;

public interface ArtworkService {

	List<Artwork> getAllArtworks();

	Artwork save(Artwork artwork);



	//void saveArtwork(Artwork artwork);


}
