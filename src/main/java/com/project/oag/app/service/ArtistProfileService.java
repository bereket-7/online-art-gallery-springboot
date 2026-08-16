package com.project.oag.app.service;

import com.project.oag.app.dto.ArtistProfileDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.ArtistFollowRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistProfileService {

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final ArtistFollowRepository artistFollowRepository;

    public ArtistProfileService(UserRepository userRepository, ArtworkRepository artworkRepository,
                                ArtistFollowRepository artistFollowRepository) {
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
        this.artistFollowRepository = artistFollowRepository;
    }

    public ArtistProfileDto getArtistProfileByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
        return buildProfile(user);
    }

    public ArtistProfileDto getArtistProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
        return buildProfile(user);
    }

    public ArtistProfileDto getArtistProfileBySlug(String slug) {
        if (slug != null && slug.chars().allMatch(Character::isDigit)) {
            return getArtistProfile(Long.parseLong(slug));
        }
        User user = userRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
        return buildProfile(user);
    }

    public Page<ArtistProfileDto> listArtists(Pageable pageable) {
        return userRepository.findUsersByRoleName("ROLE_ARTIST", pageable).map(this::buildProfile);
    }

    public List<ArtistProfileDto> getFollowedArtists(Long followerId) {
        return artistFollowRepository.findByFollowerId(followerId).stream()
                .map(follow -> buildProfile(follow.getArtist()))
                .collect(Collectors.toList());
    }

    public Page<ArtistProfileDto.ArtworkSummaryDto> getArtistPortfolio(Long artistId, Pageable pageable) {
        userRepository.findById(artistId)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
        return artworkRepository.findByUserIdAndStatus(artistId, ArtworkStatus.ACCEPTED, pageable)
                .map(this::toSummary);
    }

    private ArtistProfileDto buildProfile(User user) {
        ArtistProfileDto dto = new ArtistProfileDto();
        dto.setId(user.getId());
        dto.setUuid(user.getUuid());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBio(user.getBio());
        dto.setProfilePictureUrl(user.getImage());
        dto.setVerifiedArtist(Boolean.TRUE.equals(user.getVerifiedArtist()));
        dto.setSlug(user.getSlug());

        List<Artwork> artworks = artworkRepository.findByUserIdAndStatus(user.getId(), ArtworkStatus.ACCEPTED);
        dto.setArtworks(artworks.stream().map(this::toSummary).collect(Collectors.toList()));
        return dto;
    }

    private ArtistProfileDto.ArtworkSummaryDto toSummary(Artwork art) {
        ArtistProfileDto.ArtworkSummaryDto summary = new ArtistProfileDto.ArtworkSummaryDto();
        summary.setId(art.getId());
        summary.setTitle(art.getArtworkName());
        summary.setImageUrl(art.getImageUrls() != null && !art.getImageUrls().isEmpty()
                ? art.getImageUrls().get(0) : null);
        summary.setPrice(art.getPrice());
        summary.setCategory(art.getArtworkCategory());
        summary.setAvailable(art.getQuantity() != null && art.getQuantity() > 0);
        return summary;
    }
}
