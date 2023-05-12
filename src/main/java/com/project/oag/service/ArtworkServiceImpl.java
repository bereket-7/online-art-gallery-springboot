package com.project.oag.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.ArtworkDto;
import com.project.oag.entity.Artwork;
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
    	artwork.setArtistId(artwork.getArtistId());
        artwork.setArtworkName(artwork.getArtworkName());
        artwork.setArtworkCategory(artwork.getArtworkCategory());
        artwork.setArtworkDescription(artwork.getArtworkDescription());
        artwork.setSize(artwork.getSize());
        artwork.setPrice(artwork.getPrice());
        artwork.setStatus("pending");
        artwork.setCreateDate(artwork.getCreateDate());
        artwork.setArtworkPhoto(artwork.getArtworkPhoto());
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

	@Override
	public List<Artwork> getArtworksByArtistName(String artistName) {
		// TODO Auto-generated method stub
		return null;
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
    
    public List<ArtworkDto> getArtworkPhotoAndCategoryAndPriceAndSize(String artworkPhoto, String artworkCategory, int price, String size) {
        List<Object[]> artworkObjects = artworkRepository.findByArtworkPhotoAndArtworkCategoryAndPriceAndSize(artworkPhoto, artworkCategory, price, size);
        List<ArtworkDto> artworkDtos = new ArrayList<>();
        for (Object[] artworkObject : artworkObjects) {
            ArtworkDto artworkDto = new ArtworkDto();
            artworkDto.setArtworkPhoto((String) artworkObject[0]);
            artworkDto.setArtworkCategory((String) artworkObject[1]);
            artworkDto.setPrice((int) artworkObject[2]);
            artworkDto.setSize((String) artworkObject[3]);
            artworkDtos.add(artworkDto);
        }
        return artworkDtos;
    }
    
    public List<Artwork> getRecentArtworks() {
        return artworkRepository.findAllByOrderByCreateDateDesc();
    }
    
    /*
    @Override
    public ArtworkDto updateArtwork(Long id, ArtworkDto artworkDTO) {
        Artwork artwork = artworkRepository.findById(id).orElseThrow(() -> new ArtworkNotFoundException("Artwork not found with id " + id));
        artwork.setArtworkName(artworkDTO.getArtworkName());
        artwork.setArtworkCategory(artworkDTO.getArtworkCategory());
        artwork.setArtworkPhoto(artworkDTO.getArtworkPhoto());
        artwork.setPrice(artworkDTO.getPrice());
        artwork.setSize(artworkDTO.getSize());
        Artwork updatedArtwork = artworkRepository.save(artwork);
       return updatedArtwork;
    }*/


    
    
    
    
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

	@Override
	public Artwork getArtworkById(Long artworkId) {
        Optional<Artwork> artwork = artworkRepository.findById(artworkId);
        return artwork.get();
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

	@Override
	public ArtworkDto updateArtwork(Long id, ArtworkDto artworkDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Artwork> getArtworksByArtworkName(String artworkName) {
		// TODO Auto-generated method stub
		return null;
	}

}
