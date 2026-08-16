package com.project.oag.app.dto;

import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;

import java.math.BigDecimal;
import java.util.List;

public final class ArtworkMapper {
    private ArtworkMapper() {
    }

    public static ArtworkResponseDto toDto(Artwork artwork) {
        if (artwork == null) {
            return null;
        }
        ArtworkResponseDto dto = new ArtworkResponseDto();
        dto.setId(artwork.getId());
        dto.setArtworkName(artwork.getArtworkName());
        dto.setArtworkDescription(artwork.getArtworkDescription());
        dto.setArtworkCategory(artwork.getArtworkCategory());
        dto.setPrice(artwork.getPrice());
        dto.setSize(artwork.getSize());
        dto.setImageUrls(artwork.getImageUrls());
        dto.setStatus(artwork.getStatus());
        dto.setQuantity(artwork.getQuantity());
        dto.setMedium(artwork.getMedium());
        dto.setYearCreated(artwork.getYearCreated());
        dto.setDimensions(artwork.getDimensions());
        dto.setFraming(artwork.getFraming());
        dto.setEditionNumber(artwork.getEditionNumber());
        dto.setEditionSize(artwork.getEditionSize());
        dto.setRejectionReason(artwork.getRejectionReason());
        User artist = artwork.getUser();
        if (artist != null) {
            dto.setArtistId(artist.getId());
            dto.setArtistName(String.join(" ",
                    artist.getFirstName() == null ? "" : artist.getFirstName(),
                    artist.getLastName() == null ? "" : artist.getLastName()).trim());
            dto.setArtistSlug(artist.getSlug());
        }
        if (artwork.getRatings() != null
                && org.hibernate.Hibernate.isInitialized(artwork.getRatings())
                && !artwork.getRatings().isEmpty()) {
            dto.setAverageRating(artwork.getRatings().stream()
                    .mapToDouble(r -> r.getRatingValue())
                    .average()
                    .orElse(0));
        }
        dto.setAllowOffers(artwork.getPrice() != null && artwork.getPrice().compareTo(BigDecimal.ZERO) > 0);
        return dto;
    }

    public static List<ArtworkResponseDto> toDtoList(List<Artwork> artworks) {
        return artworks.stream().map(ArtworkMapper::toDto).toList();
    }
}
