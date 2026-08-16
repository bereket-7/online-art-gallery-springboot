package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkMapper;
import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.CollectionResponseDto;
import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.ArtworkViewRepository;
import com.project.oag.app.repository.CollectionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiscoveryService {

    private final CollectionRepository collectionRepository;
    private final ArtworkViewRepository artworkViewRepository;
    private final ArtworkRepository artworkRepository;
    private final ModelMapper modelMapper;

    public DiscoveryService(CollectionRepository collectionRepository,
                            ArtworkViewRepository artworkViewRepository,
                            ArtworkRepository artworkRepository,
                            ModelMapper modelMapper) {
        this.collectionRepository = collectionRepository;
        this.artworkViewRepository = artworkViewRepository;
        this.artworkRepository = artworkRepository;
        this.modelMapper = modelMapper;
    }

    public List<CollectionResponseDto> getFeaturedCollections() {
        return collectionRepository.findByFeaturedTrue().stream()
                .map(c -> CommerceMappers.toCollectionDto(c, false))
                .toList();
    }

    public List<CollectionResponseDto> getAllCollections() {
        return collectionRepository.findAll().stream()
                .map(c -> CommerceMappers.toCollectionDto(c, false))
                .toList();
    }

    public List<ArtworkResponseDto> getTrendingArtworks(int limit) {
        List<Object[]> trending = artworkViewRepository.findTrendingArtworkIds();
        List<Long> ids = trending.stream()
                .limit(limit)
                .map(row -> (Long) row[0])
                .collect(Collectors.toCollection(ArrayList::new));

        if (ids.isEmpty()) {
            return List.of();
        }

        Map<Long, Artwork> artworkMap = artworkRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Artwork::getId, a -> a));

        return ids.stream()
                .filter(artworkMap::containsKey)
                .map(id -> ArtworkMapper.toDto(artworkMap.get(id)))
                .collect(Collectors.toList());
    }
}
