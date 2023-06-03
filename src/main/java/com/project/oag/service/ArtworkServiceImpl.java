package com.project.oag.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.ArtworkDto;
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
	public List<Artwork> getAllArtworks() {
		return artworkRepository.findAll();
	}
	@Override
	public Optional<Artwork> getArtworkById(Long id) {
		return artworkRepository.findById(id);
	}
	@Override
	public void saveArtwork(Artwork artwork) {
		artworkRepository.save(artwork);	
	}
	@Override
	public void deleteArtwork(Long id) {
		artworkRepository.deleteById(id);
	}
	@Override
	public List<Artwork> getArtworkByCategory(String artworkCategory) {
		return artworkRepository.findByArtworkCategory(artworkCategory);
	}
	@Override
	public List<Artwork> getArtworksByPriceRange(int minPrice, int maxPrice) {
		return artworkRepository.findByPriceBetween(minPrice, maxPrice);
	}
    public List<Artwork> getPendingArtworks() {
        return artworkRepository.findByStatus("pending");
    }
	public List<Artwork> getAcceptedArtworks() {
		 return artworkRepository.findByStatus("accepted");
	}
	public List<Artwork> getRejectedArtworks() {
		 return artworkRepository.findByStatus("rejected");
	}
    @Transactional
    public boolean acceptArtwork(Long id) {
        Optional<Artwork> artworkOptional = artworkRepository.findById(id);
        if (artworkOptional.isPresent()) {
            Artwork artwork = artworkOptional.get();
            if (artwork.getStatus().equals("pending")) {
                artwork.setStatus("accepted");
                artworkRepository.save(artwork);
                return true;
            }
        }
        return false;
    }
    @Transactional
	public boolean rejectArtwork(Long id) {
    	 Optional<Artwork> artworkOptional = artworkRepository.findById(id);
         if (artworkOptional.isPresent()) {
             Artwork artwork = artworkOptional.get();
             if (artwork.getStatus().equals("pending")) {
                 artwork.setStatus("rejected");
                 artworkRepository.save(artwork);
                 return true;
             }
         }
         return false;
	}
    public List<Artwork> getRecentArtworks() {
        return artworkRepository.findAllByOrderByCreateDateDesc();
    }
	@Override
	public ArtworkDto getDtoFromArtwork(Artwork artwork) {
	    ArtworkDto artworkDto = new ArtworkDto(artwork);
        return artworkDto;
	}
	@Override
	public List<Artwork> getArtworksByArtistId(int artistId) {
		return artworkRepository.findByArtistId(artistId);
	}

}
