package com.project.oag.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Artwork;
import com.project.oag.repository.ArtworkRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArtworkServiceImpl implements ArtworkService{
	@Autowired
	private ArtworkRepository artworkRepository;

	public ArtworkServiceImpl(ArtworkRepository artworkRepository) {
		super();
		this.artworkRepository = artworkRepository;
	}
	
	public void saveArtwork(Artwork artwork) {
		artworkRepository.save(artwork);	
	}

	public List<Artwork> getAllArtworks() {
		return artworkRepository.findAll();
	}

	public Optional<Artwork> getArtworkById(Long id) {
		return artworkRepository.findById(id);
	}

	/**
	@Override
	public Artwork  saveArtwork(ArtworkDto artworkDto,@RequestParam("artworkPhoto") MultipartFile multipartFile) throws IOException {
		Artwork artwork = new Artwork();
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		artworkDto.setArtworkName(artworkDto.getArtworkName());
		artworkDto.setArtworkDescription(artworkDto.getArtworkDescription());
		artworkDto.setArtworkCategory(artworkDto.getArtworkCategory());
		artworkDto.setPrice(artworkDto.getPrice());
		artworkDto.setArtistName(artworkDto.getArtistName());
		artworkDto.setStatus(artworkDto.getStatus());
		artworkDto.setTimestamp(artworkDto.getTimestamp());
		//artworkDto.setArtworkPhoto(fileName)
		artworkDto.setArtworkPhoto(fileName);
		//return artworkRepository.save(artwork);
		//String uploadDir = "arts/" + artwork.getId();
		//FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		return artworkRepository.save(artwork);
	}**/
	/**
	@Override 
	public Artwork uploadArtwork(ArtworkDto artworkDto) {
		Artwork artwork = new Artwork(artworkDto.getArtworkName(),artworkDto.getArtworkDescription(),
				artworkDto.getArtworkCategory(),artworkDto.getArtworkPhoto(),artworkDto.getPrice(),
				artworkDto.getTimestamp(),artworkDto.getStatus(),artworkDto.getArtistName());
		return artworkRepository.save(artwork);
	}
	@Override
	public Artwork updateArtwork(ArtworkDto artworkDto)throws UserNotFoundException {
	     Artwork updateArtwork = artworkRepository.findById(artworkDto.getArtworkName());
	     updateArtwork.setArtworkName(artworkDto.getArtworkName());
	     updateArtwork.setArtworkDescription(artworkDto.getArtworkDescription());
	     updateArtwork.setArtworkCategory(artworkDto.getArtworkCategory());
	     updateArtwork.setArtworkPhoto(artworkDto.getArtworkPhoto());
	     updateArtwork.setPrice(artworkDto.getPrice());
	     return artworkRepository.save(updateArtwork);
	}*/
	/*
	@Override
	 public List<Artwork> searchByCategory(String artworkCategory) {
	     return artworkRepository.findByCategory(artworkCategory);
	}
	@Override
	public Artwork findById(Long artworkId) {
		// TODO Auto-generated method stub
		return artworkRepository.getById(artworkId);
	}
	@Override
	public Artwork getArtworkById(Long artworkId) {
		// TODO Auto-generated method stub
		return artworkRepository.getById(artworkId);
	}*/
}
