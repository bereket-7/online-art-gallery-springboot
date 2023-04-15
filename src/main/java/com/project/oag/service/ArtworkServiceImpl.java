package com.project.oag.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Artwork;
import com.project.oag.repository.ArtworkRepository;
import com.project.oag.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArtworkServiceImpl implements ArtworkService{
	@Autowired
	private ArtworkRepository artworkRepository;
	
	@Autowired
	private UserRepository userRepository;

	public ArtworkServiceImpl(ArtworkRepository artworkRepository) {
		super();
		this.artworkRepository = artworkRepository;
	}
	  @Override
	    public Artwork save(Artwork artwork) {
	        return artworkRepository.save(artwork);
	    }
	public List<Artwork> getAllArtworks() {
		return artworkRepository.findAll();
	}
	/*
    @Override
    public void saveArtwork(Artwork artwork) {
        //System.out.println(user.getId());
        //artwork.setArtistId(user.getId());
    	artwork.setArtistId(artwork.getArtistId());
        artwork.setArtworkName(artwork.getArtworkName());
        artwork.setArtworkCategory(artwork.getArtworkCategory());
        artwork.setArtworkDescription(artwork.getArtworkDescription());
        artwork.setSize(artwork.getSize());
        artwork.setPrice(artwork.getPrice());
        artwork.setStatus("pending");
        artwork.setCreateDate(artwork.getCreateDate());
        String artworkPhoto = "/img/artworks/" + artwork.getId() + "/" + artwork.getArtworkPhoto();
        artwork.setArtworkPhoto(artworkPhoto);
        System.out.println(artwork.getArtworkPhoto());
        artworkRepository.save(artwork);
    }*/
/*
    @Override
    public Artwork findArtworkById(int id) {
        return artworkRepository.findArtworkById(id);
    }

    @Override
    public List<Artwork> findArtworkByOwner(int id) {
        return artworkRepository.findArtworkByOwner(id);
    }

    @Override
    public void updateArtwork(int id) {
        artworkRepository.updateArtwork(id);
    }

    /*
    @Override
    public void updateArtworkLikes(int id, int likes) {
        artworkRepository.updateArtworkLikes(id, likes);
    }

    @Override
    public String getArtOwnerName(Artwork artwork) {
        User user = userRepository.findByUserId(artwork.getArtistId());
        String name = user.getFirstname();
        return name;
    }
*/
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
