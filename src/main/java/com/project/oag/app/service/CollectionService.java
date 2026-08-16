package com.project.oag.app.service;

import com.project.oag.app.dto.CollectionResponseDto;
import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.entity.Collection;
import com.project.oag.app.repository.CollectionRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Transactional(readOnly = true)
    public List<CollectionResponseDto> list(boolean featuredOnly) {
        List<Collection> collections = featuredOnly
                ? collectionRepository.findByFeaturedTrue()
                : collectionRepository.findAll();
        return collections.stream()
                .map(c -> CommerceMappers.toCollectionDto(c, false))
                .toList();
    }

    @Transactional(readOnly = true)
    public CollectionResponseDto getBySlug(String slug) {
        Collection collection;
        if (slug != null && slug.chars().allMatch(Character::isDigit)) {
            collection = collectionRepository.findById(Long.parseLong(slug))
                    .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));
        } else {
            collection = collectionRepository.findBySlug(slug)
                    .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));
        }
        collection.getArtworks().size();
        return CommerceMappers.toCollectionDto(collection, true);
    }
}
