package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkMapper;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ImageUtils imageUtils;
    private final ArtworkViewService artworkViewService;

    public ArtworkService(ArtworkRepository artworkRepository, UserRepository userRepository,
                          ModelMapper modelMapper, ImageUtils imageUtils,
                          ArtworkViewService artworkViewService) {
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.imageUtils = imageUtils;
        this.artworkViewService = artworkViewService;
    }

    @Transactional
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
        artwork.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 1);
        artwork.setMedium(dto.getMedium());
        artwork.setYearCreated(dto.getYearCreated());
        artwork.setDimensions(dto.getDimensions());
        artwork.setFraming(dto.getFraming());
        artwork.setEditionNumber(dto.getEditionNumber());
        artwork.setEditionSize(dto.getEditionSize());

        Artwork saved = artworkRepository.save(artwork);
        return ArtworkMapper.toDto(saved);
    }

    public List<ArtworkResponseDto> getAllArtworks() {
        return ArtworkMapper.toDtoList(artworkRepository.findAll());
    }

    public List<ArtworkResponseDto> getAcceptedArtworks() {
        return ArtworkMapper.toDtoList(artworkRepository.findByStatus(ArtworkStatus.ACCEPTED));
    }

    @Transactional(readOnly = true)
    public ArtworkResponseDto getArtworkById(Long id) {
        val artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        return ArtworkMapper.toDto(artwork);
    }

    public ArtworkResponseDto getArtworkById(Long id, Long viewerUserId) {
        artworkViewService.recordView(id, viewerUserId);
        return getArtworkById(id);
    }

    @Transactional
    public ArtworkResponseDto updateArtwork(Long id, ArtworkRequestDto dto) {
        if (ObjectUtils.isEmpty(id))
            throw new GeneralException("Artwork id must not be empty");
        val artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        modelMapper.map(dto, artwork);
        val saved = artworkRepository.save(artwork);
        log.info(LOG_PREFIX, "Updated artwork", id);
        return ArtworkMapper.toDto(saved);
    }

    @Transactional
    public void deleteArtwork(Long id) {
        artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        artworkRepository.deleteById(id);
    }

    @Transactional
    public ArtworkResponseDto changeArtworkStatus(Long id, ArtworkStatus status) {
        return changeArtworkStatus(id, status, null);
    }

    @Transactional
    public ArtworkResponseDto changeArtworkStatus(Long id, ArtworkStatus status, String rejectionReason) {
        val artwork = artworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        artwork.setStatus(status);
        if (rejectionReason != null) {
            artwork.setRejectionReason(rejectionReason);
        }
        return ArtworkMapper.toDto(artworkRepository.save(artwork));
    }

    public List<ArtworkResponseDto> getArtworkByStatus(ArtworkStatus status) {
        return ArtworkMapper.toDtoList(artworkRepository.findByStatus(status));
    }

    public List<ArtworkResponseDto> getLoggedArtistArtworks(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        return ArtworkMapper.toDtoList(artworkRepository.findByArtistId(userId));
    }

    public List<ArtworkResponseDto> getArtworkByCategory(String artworkCategory) {
        return ArtworkMapper.toDtoList(artworkRepository.findByArtworkCategory(artworkCategory));
    }

    public Map.Entry<List<ArtworkResponseDto>, PageableDto> getRecentArtworks(Pageable pageable) {
        Page<Artwork> page = artworkRepository.findByStatusOrderByCreationDateDesc(ArtworkStatus.ACCEPTED, pageable);
        return Map.entry(ArtworkMapper.toDtoList(page.getContent()), preparePageInfo(page));
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
        List<ArtworkResponseDto> content = ArtworkMapper.toDtoList(page.getContent());
        return Map.entry(content, preparePageInfo(page));
    }

    /**
     * Decrements the available quantity of an artwork after a successful purchase.
     * Throws GeneralException if stock would go negative.
     */
    @Transactional
    public void decrementQuantity(Long artworkId, int qty) {
        val artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found: " + artworkId));
        int current = artwork.getQuantity() == null ? 0 : artwork.getQuantity();
        if (current < qty) {
            throw new GeneralException("Insufficient stock for artwork " + artworkId);
        }
        artwork.setQuantity(current - qty);
        artworkRepository.save(artwork);
        log.info(LOG_PREFIX, "Artwork quantity decremented",
                "artworkId=" + artworkId + " decremented by " + qty + ", remaining=" + artwork.getQuantity());
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
