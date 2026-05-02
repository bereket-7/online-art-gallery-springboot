package com.project.oag.app.service;

import com.project.oag.app.dto.ArtistProfileDto;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistProfileService {

    private final UserRepository userRepository;

    public ArtistProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ArtistProfileDto getArtistProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found"));
        
        // Ensure user is actually an artist if necessary
        // if (!"ARTIST".equalsIgnoreCase(user.getUserRole().getRoleName())) throw ...

        ArtistProfileDto dto = new ArtistProfileDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBio(user.getBio());
        dto.setProfilePictureUrl(user.getImage());
        
        if (user.getArtwork() != null) {
            List<ArtistProfileDto.ArtworkSummaryDto> summaries = user.getArtwork().stream()
                .map(art -> {
                    ArtistProfileDto.ArtworkSummaryDto summary = new ArtistProfileDto.ArtworkSummaryDto();
                    summary.setId(art.getId());
                    summary.setTitle(art.getTitle());
                    summary.setImageUrl(art.getImageUrl());
                    summary.setPrice(art.getPrice());
                    summary.setCategory(art.getCategory() != null ? art.getCategory().getCategoryName() : null);
                    summary.setAvailable(art.getQuantity() > 0);
                    return summary;
                }).collect(Collectors.toList());
            dto.setArtworks(summaries);
        }
        
        return dto;
    }
}
