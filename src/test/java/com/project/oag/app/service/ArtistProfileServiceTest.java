package com.project.oag.app.service;

import com.project.oag.app.dto.ArtistProfileDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistProfileServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ArtworkRepository artworkRepository;

    @InjectMocks
    private ArtistProfileService artistProfileService;

    private User artistUser;
    private Artwork sampleArtwork;

    @BeforeEach
    void setUp() {
        artistUser = new User();
        artistUser.setId(10L);
        artistUser.setFirstName("John");
        artistUser.setLastName("Doe");
        artistUser.setBio("A wonderful native artist");
        artistUser.setImage("http://profile.url");

        sampleArtwork = new Artwork();
        sampleArtwork.setId(100L);
        sampleArtwork.setArtworkName("Mona Lisa Mock");
        sampleArtwork.setQuantity(1);
        sampleArtwork.setPrice(BigDecimal.TEN);
        sampleArtwork.setArtworkCategory("Painting");
        sampleArtwork.setImageUrls(List.of("http://img.url"));
    }

    @Test
    void getArtistProfile_ShouldMapSuccessfully() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(artistUser));
        when(artworkRepository.findByUserIdAndStatus(10L, ArtworkStatus.ACCEPTED))
                .thenReturn(List.of(sampleArtwork));

        ArtistProfileDto response = artistProfileService.getArtistProfile(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("A wonderful native artist", response.getBio());
        assertEquals(1, response.getArtworks().size());
        assertEquals("Mona Lisa Mock", response.getArtworks().get(0).getTitle());
        assertTrue(response.getArtworks().get(0).isAvailable());
    }

    @Test
    void getArtistProfile_ThrowsNotFound_WhenIdMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> artistProfileService.getArtistProfile(99L));
    }
}
