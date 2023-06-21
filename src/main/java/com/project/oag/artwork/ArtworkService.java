package com.project.oag.artwork;
import com.project.oag.user.User;
import org.springframework.data.domain.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.project.oag.user.UserRepository;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class  ArtworkService{
	@Autowired
	private ArtworkRepository artworkRepository;

	public ArtworkService(ArtworkRepository artworkRepository) {
		this.artworkRepository = artworkRepository;
	}

	@Autowired
	private UserRepository userRepository;


	public List<Artwork> getAllArtworks() {
		return artworkRepository.findAll();
	}

	public Optional<Artwork> getArtworkById(Long id) {
		return artworkRepository.findById(id);
	}

	public void saveArtwork(Artwork artwork) {
		artworkRepository.save(artwork);	
	}

	public void deleteArtwork(Long id) {
		artworkRepository.deleteById(id);
	}

	public List<Artwork> getArtworkByCategory(String artworkCategory) {
		return artworkRepository.findByArtworkCategory(artworkCategory);
	}

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

	public ArtworkDto getDtoFromArtwork(Artwork artwork) {
	    ArtworkDto artworkDto = new ArtworkDto(artwork);
        return artworkDto;
	}
	public List<Artwork> getArtworksByArtistId(int artistId) {
		return artworkRepository.findByArtistId(artistId);
	}
	public Map<String, Integer> getCountByCategory() {
		List<Object[]> countByCategory = artworkRepository.countByCategory();
		Map<String, Integer> result = new HashMap<>();
		for (Object[] obj : countByCategory) {
			String category = (String) obj[0];
			Integer count = ((Number) obj[1]).intValue();
			result.put(category, count);
		}
		return result;
	}
	public List<Artwork> getArtworksForLoggedArtist(User artist) {
		return artworkRepository.findByArtist(artist);
	}

	public List<Artwork> searchArtwork(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return artworkRepository.searchArtwork(keyword, pageable);
	}
	public List<String> getAutocompleteResults(String keyword) {
		return artworkRepository.getAutocompleteResults(keyword);
	}

	public List<Artwork> getSortedArtworks(String sortOption) {
		switch (sortOption) {
			case "rating":
				return artworkRepository.findAllByOrderByAverageRatingDesc();
			case "priceLowToHigh":
				return artworkRepository.findAllByOrderByPriceAsc();
			case "priceHighToLow":
				return artworkRepository.findAllByOrderByPriceDesc();
			case "latest":
				return artworkRepository.findAllByOrderByCreateDateDesc();
			default:
				throw new IllegalArgumentException("Invalid sort option: " + sortOption);
		}
	}

}
