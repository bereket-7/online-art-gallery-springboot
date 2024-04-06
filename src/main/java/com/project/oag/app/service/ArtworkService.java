package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.model.Artwork;
import com.project.oag.app.model.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.ImageUtils.saveImagesAndGetUrls;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class  ArtworkService{
	private final ArtworkRepository artworkRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public ArtworkService(ArtworkRepository artworkRepository, UserRepository userRepository, ModelMapper modelMapper) {
		this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

	public ResponseEntity<GenericResponse> saveArtwork(HttpServletRequest request,ArtworkRequestDto artworkRequestDto) {
		Long userId = getUserId(request);
		List<String> imageUrls = saveImagesAndGetUrls(artworkRequestDto.getImageFiles());
		try {
			Artwork artwork = new Artwork();
			artwork.setArtworkName(artworkRequestDto.getArtworkName());
			artwork.setArtworkCategory(artworkRequestDto.getArtworkCategory());
			artwork.setArtworkDescription(artworkRequestDto.getArtworkDescription());
			artwork.setStatus(ArtworkStatus.PENDING);
			artwork.setPrice(artworkRequestDto.getPrice());
			artwork.setSize(artworkRequestDto.getSize());
			artwork.setArtistId(userId);
			artwork.setImageUrls(imageUrls);
			val response = artworkRepository.save(artwork);
			return prepareResponse(HttpStatus.OK,"success",response);
		} catch (Exception e) {
			throw new GeneralException("Error saving artwork");
		}
	}

	public ResponseEntity<GenericResponse> getAllArtworks() {
		try {
			val response = artworkRepository.findAll();
			return prepareResponse(HttpStatus.OK,"Successfully retrieved all artworks",response);
		} catch (Exception e) {
			throw new GeneralException("Failed to get artworks");
		}
	}
	public ResponseEntity<GenericResponse> getArtworkById(Long id) {
		try {
			val response = artworkRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("artwork record not found"));
			return prepareResponse(HttpStatus.OK,"Successfully retrieved artwork",response);
		} catch (Exception e) {
			throw new GeneralException("Failed to get artwork ");
		}
	}

	public ResponseEntity<GenericResponse> updateArtwork(Long id, ArtworkRequestDto artworkRequestDto) {
		if (ObjectUtils.isEmpty(id))
			throw new GeneralException("artwork Id needs to have a value");
		val artwork = artworkRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Artwork record not found"));
		try {
			modelMapper.map(artworkRequestDto, artwork);
			val response = artworkRepository.save(artwork);
			log.info(LOG_PREFIX, "Saved artwork information", "");
			return prepareResponse(HttpStatus.OK, "Saved artwork ", response);
		}catch (Exception e){
			throw new GeneralException("Failed to save artwork information");
		}
	}
	public ResponseEntity<GenericResponse> deleteArtwork(final Long id) {
		try {
			artworkRepository.deleteById(id);
			return prepareResponse(HttpStatus.OK, "Artwork Successfully deleted", null);
		} catch (Exception e) {
			throw new GeneralException("Failed to delete artwork");
		}
	}

	public ResponseEntity<GenericResponse> changeArtworkStatus(Long id, ArtworkStatus status) {
		try {
			val artwork = artworkRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Artwork record not found"));
			artwork.setStatus(status);
			artworkRepository.save(artwork);
			return prepareResponse(HttpStatus.OK,"Artwork Status Successfully Updated",artwork);
		} catch (Exception e) {
			throw new GeneralException("failed to update artwork status");
		}
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

	public ArtworkRequestDto getDtoFromArtwork(Artwork artwork) {
	    ArtworkRequestDto artworkRequestDto = new ArtworkRequestDto(artwork);
        return artworkRequestDto;
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


	private Long getUserId(HttpServletRequest request) {
		return getUserByUsername(getLoggedInUserName(request)).getId();
	}

	private User getUserByUsername(String email) {
		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
	}

}
