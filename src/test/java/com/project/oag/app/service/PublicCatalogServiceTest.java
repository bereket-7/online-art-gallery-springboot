package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.repository.ArtworkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicCatalogServiceTest {

    @Mock
    private ArtworkRepository artworkRepository;
    @Mock
    private com.project.oag.app.repository.UserRepository userRepository;
    @Mock
    private org.modelmapper.ModelMapper modelMapper;
    @Mock
    private com.project.oag.utils.ImageUtils imageUtils;
    @Mock
    private ArtworkViewService artworkViewService;

    @InjectMocks
    private ArtworkService artworkService;

    @Test
    void getRecentArtworks_ReturnsAcceptedOnlyWithoutAuth() {
        when(artworkRepository.findByStatusOrderByCreationDateDesc(ArtworkStatus.ACCEPTED, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));

        var result = artworkService.getRecentArtworks(PageRequest.of(0, 20));

        assertEquals(0, result.getKey().size());
    }
}
