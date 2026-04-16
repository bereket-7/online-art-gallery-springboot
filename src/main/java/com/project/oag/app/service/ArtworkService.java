package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.dto.PageableDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.helper.ArtworkFilterSpecification;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.utils.ImageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.PageableUtils.preparePageInfo;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ImageUtils imageUtils;

    public ArtworkService(ArtworkRepository artworkRepository, UserRepository userRepository,
                          ModelMapper modelMapper, ImageUtils imageUtils) {
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.imageUtils = imageUtils;
    }

    public ArtworkResponseDto saveArtwork(HttpServletRequest request, ArtworkRequestDto dto) throws IOException {
        User user = getUserByUsername(getLoggedInUserName(request));
        List<String> imageUrls = imageUtils.saveImagesAndGetUrls(dto.getImageFiles());

        Artwork artwork = new Artwork();
        artwork.setArtworkName(dto.getArtworkName());
        artwork.setArtworkCategory(dto.getArtworkCategory());
        artwork.setArtworkDescription(dto.getArtworkDescription());
        artwork.setStatus(ArtworkStatus.PENDING);
        artwork.setPrice(dto.getPrice());
        artwork.setSize(dto.getSize());
        artwork.setImageUrls(imageUrls);
        artwork.setUser(user);

        Artwork saved = artworkRepository.save(artwork);
        return modelMapper.map(saved, ArtworkResponseDto.class);
    }

    public List<ArtworkResponseDto> getAllArtworks() {
        return artworkRepository.findAll().stream()
                .map(a -> modelMapper.map(a, ArtworkResponseDto.class))
                .collect(Collectors.toList());
    }

    public ArtworkResponseDto getArtworkById(Long id) {
        val artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        return modelMapper.map(artwork, ArtworkResponseDto.class);
    }

    public ArtworkResponseDto updateArtwork(Long id, ArtworkRequestDto dto) {
        if (ObjectUtils.isEmpty(id))
            throw new GeneralException("Artwork id must not be empty");
        val artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        modelMapper.map(dto, artwork);
        val saved = artworkRepository.save(artwork);
        log.info(LOG_PREFIX, "Updated artwork", id);
        return modelMapper.map(saved, ArtworkResponseDto.class);
    }

    public void deleteArtwork(Long id) {
        artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        artworkRepository.deleteById(id);
    }

    public ArtworkResponseDto changeArtworkStatus(Long id, ArtworkStatus status) {
        val artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        artwork.setStatus(status);
        return modelMapper.map(artworkRepository.save(artwork), ArtworkResponseDto.class);
    }

    public List<ArtworkResponseDto> getArtworkByStatus(ArtworkStatus status) {
        return artworkRepository.findByStatus(status).stream()
                .map(a -> modelMapper.map(a, ArtworkResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ArtworkResponseDto> getLoggedArtistArtworks(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        return artworkRepository.findByArtistId(userId).stream()
                .map(a -> modelMapper.map(a, ArtworkResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ArtworkResponseDto> getArtworkByCategory(String artworkCategory) {
        return artworkRepository.findByArtworkCategory(artworkCategory).stream()
                .map(a -> modelMapper.map(a, ArtworkResponseDto.class))
                .collect(Collectors.toList());
    }

    public Map.Entry<List<ArtworkResponseDto>, PageableDto> getRecentArtworks(Pageable pageable) {
        Page<ArtworkResponseDto> page = artworkRepository.findRecentArtworks(pageable);
        return Map.entry(page.getContent(), preparePageInfo(page));
    }

    public List<Object[]> getCountByCategory() {
        return artworkRepository.countByCategory();
    }

    public Map.Entry<List<ArtworkResponseDto>, PageableDto> searchArtwork(
            String artworkCategory, String artworkName, BigDecimal minPrice, BigDecimal maxPrice,
            String sortBy, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {

        Specification<Artwork> spec = ArtworkFilterSpecification.searchArtworks(
                artworkCategory, artworkName, minPrice, maxPrice, sortBy, fromDate, toDate);
        Page<Artwork> page = artworkRepository.findAll(spec, pageable);
        List<ArtworkResponseDto> content = page.getContent().stream()
                .map(a -> modelMapper.map(a, ArtworkResponseDto.class))
                .collect(Collectors.toList());
        return Map.entry(content, preparePageInfo(page));
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
