package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.helper.ArtworkFilterSpecification;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.ImageUtils.saveImagesAndGetUrls;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class ArtworkService {
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ArtworkService(ArtworkRepository artworkRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<GenericResponse> saveArtwork(HttpServletRequest request, ArtworkRequestDto artworkRequestDto) throws IOException {
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
            //artwork.setArtistId(userId);
            artwork.setImageUrls(imageUrls);
            val response = artworkRepository.save(artwork);
            return prepareResponse(HttpStatus.OK, "success", response);
        } catch (Exception e) {
            throw new GeneralException("Error saving artwork");
        }
    }

    public ResponseEntity<GenericResponse> getAllArtworks() {
        try {
            val response = artworkRepository.findAll();
            List<ArtworkResponseDto> artworks = response.stream().map((element) -> modelMapper.map(element, ArtworkResponseDto.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Successfully retrieved all artworks", artworks);
        } catch (Exception e) {
            throw new GeneralException("Failed to get artworks");
        }
    }

    public ResponseEntity<GenericResponse> getArtworkById(Long id) {
        try {
            val response = artworkRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("artwork record not found"));
            return prepareResponse(HttpStatus.OK, "Successfully retrieved artwork", response);
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
        } catch (Exception e) {
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
            return prepareResponse(HttpStatus.OK, "Artwork Status Successfully Updated", artwork);
        } catch (Exception e) {
            throw new GeneralException("failed to update artwork status");
        }
    }

    public ResponseEntity<GenericResponse> getArtworkByArtworkStatus(ArtworkStatus status) {
        try {
            val response = artworkRepository.findByStatus(status);
            List<ArtworkResponseDto> artworks = response.stream().map((element) -> modelMapper.map(element, ArtworkResponseDto.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Successfully retrieved events", artworks);
        } catch (Exception e) {
            throw new GeneralException(" failed to get artworks by status ");
        }
    }

    public ResponseEntity<GenericResponse> getLoggedArtistArtworks(HttpServletRequest request) {
        Long userId = getUserId(request);
        try {
            val response = artworkRepository.findByArtistId(userId);
            List<ArtworkResponseDto> artworks = response.stream().map((element) -> modelMapper.map(element, ArtworkResponseDto.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Successfully retrieved all artworks", artworks);
        } catch (Exception e) {
            throw new GeneralException("Failed to retrieve artworks");
        }
    }

    public ResponseEntity<GenericResponse> getArtworkByArtworkCategory(String artworkCategory) {
        try {
            val response = artworkRepository.findByArtworkCategory(artworkCategory);
            List<ArtworkResponseDto> artworks = response.stream().map((element) -> modelMapper.map(element, ArtworkResponseDto.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Successfully retrieved", artworks);
        } catch (Exception e) {
            throw new GeneralException(" failed to get artworks by status ");
        }
    }

    public ResponseEntity<GenericResponse> getRecentArtworks() {
        try {
            val response = artworkRepository.findRecentArtworks();
            List<ArtworkResponseDto> artworks = response.stream().map((element) -> modelMapper.map(element, ArtworkResponseDto.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Successfully retrieved all artworks", artworks);
        } catch (Exception e) {
            throw new GeneralException("Failed to get artworks");
        }
    }

    public ResponseEntity<GenericResponse> getCountByCategory() {
        try {
            List<Object[]> response = artworkRepository.countByCategory();
            return prepareResponse(HttpStatus.OK, "Successfully retrieved", response);
        } catch (Exception e) {
            throw new GeneralException("failed to retrieve category count");
        }
    }

    public ResponseEntity<GenericResponse> searchArtwork(String artworkCategory, String artworkName, BigDecimal minPrice, BigDecimal maxPrice, String sortBy,
                                                         LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        Specification<Artwork> spec = ArtworkFilterSpecification.searchArtworks(artworkCategory, artworkName, minPrice, maxPrice, sortBy, fromDate, toDate);

        try {
            Page<Artwork> artworks = artworkRepository.findAll(spec, pageable);
            List<ArtworkResponseDto> artworkResponseDto = artworks.getContent().stream()
                    .map(artwork -> modelMapper.map(artwork, ArtworkResponseDto.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Available artworks ", artworkResponseDto);
        } catch (Exception e) {
            log.error("Unable to search artworks", e);
            throw new GeneralException("Unable to search artworks");
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
