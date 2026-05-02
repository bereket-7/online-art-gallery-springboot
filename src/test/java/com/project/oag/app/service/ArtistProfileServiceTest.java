package com.project.oag.app.service;

import com.project.oag.app.dto.ArtistProfileDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistProfileServiceTest {

    @Mock
    private UserRepository userRepository;

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
        sampleArtwork.setTitle("Mona Lisa Mock");
        sampleArtwork.setQuantity(1);

        artistUser.setArtwork(Collections.singletonList(sampleArtwork));
    }

    @Test
    void getArtistProfile_ShouldMapSuccessfully() {
        // Arrange
        when(userRepository.findById(10L)).thenReturn(Optional.of(artistUser));

        // Act
        ArtistProfileDto response = artistProfileService.getArtistProfile(10L);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("A wonderful native artist", response.getBio());
        assertEquals(1, response.getArtworks().size());
        assertEquals("Mona Lisa Mock", response.getArtworks().get(0).getTitle());
        assertTrue(response.getArtworks().get(0).isAvailable());

        verify(userRepository, times(1)).findById(10L);
    }

    @Test
    void getArtistProfile_ThrowsNotFound_WhenIdMissing() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            artistProfileService.getArtistProfile(99L);
        });
    }
}
