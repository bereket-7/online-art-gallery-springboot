package com.project.oag.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.ArtworkDto;
import com.project.oag.entity.Artwork;
import com.project.oag.entity.Event;
import com.project.oag.exceptions.ArtworkNotFoundException;
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
	
	//@Autowired
	//private RatingRepository ratingRepository;
	
	public List<Artwork> getAllArtworks() {
		return artworkRepository.findAll();
	}
	
	
	
	@Override
	public void saveArtwork(Artwork artwork) {
		artworkRepository.save(artwork);	
	}

	@Override
	public void deleteArtwork(Long id) {
		artworkRepository.deleteById(id);// TODO Auto-generated method stub
		
	}

	@Override
	public List<Artwork> getArtworkByCategory(String artworkCategory) {
		return artworkRepository.findByArtworkCategory(artworkCategory);
	}

    public List<Artwork> getArtworkByPriceRange(double minPrice, double maxPrice) {
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
    
    
/*	@Override
    public Double getAverageRating(Long artworkId) {
        List<Rating> ratings = ratingRepository.findByArtwork(artworkId);
        if (ratings.isEmpty()) {
            return null;
        } else {
            double sum = 0;
            for (Rating rating : ratings) {
                sum += rating.getRating();
            }
            return sum / ratings.size();
        }
    }*/
	

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


	@Override
	public Artwork getArtworkById(Long artworkId) {
        Optional<Artwork> artwork = artworkRepository.findById(artworkId);
        return artwork.get();
    }

	@Override
	public List<Artwork> getArtworksByArtistId(int artistId) {
		return artworkRepository.findByArtistId(artistId);
	}

}
