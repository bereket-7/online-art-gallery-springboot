package com.project.oag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Artwork;
import com.project.oag.repository.ArtworkRepository;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {
	 
	 @Autowired
	 private ArtworkRepository artworkRepository;
	 
	    @GetMapping("/{artistName}/artworks")
	    public List<Artwork> getArtworksByArtistAndStatus(@PathVariable Long artistName) {
	        return artworkRepository.findByIdAndStatus(artistName, "verified");
	    }
	    
	    
}
