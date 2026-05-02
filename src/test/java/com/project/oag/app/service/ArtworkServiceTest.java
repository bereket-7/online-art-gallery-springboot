package com.project.oag.app.service;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkResponseDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.utils.ImageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtworkServiceTest {

    @Mock
    private ArtworkRepository artworkRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ImageUtils imageUtils;

    @InjectMocks
    private ArtworkService artworkService;

    private Artwork sampleArtwork;

    @BeforeEach
    void setUp() {
        sampleArtwork = new Artwork();
        sampleArtwork.setId(5L);
        sampleArtwork.setQuantity(10);
        sampleArtwork.setStatus(ArtworkStatus.PENDING);
    }

    @Test
    void decrementQuantity_DecrementsSuccessfully_WhenStockAvailable() {
        // Arrange
        when(artworkRepository.findById(5L)).thenReturn(Optional.of(sampleArtwork));
        when(artworkRepository.save(any(Artwork.class))).thenReturn(sampleArtwork);

        // Act
        artworkService.decrementQuantity(5L, 3);

        // Assert
        assertEquals(7, sampleArtwork.getQuantity());
        verify(artworkRepository, times(1)).save(sampleArtwork);
    }

    @Test
    void decrementQuantity_ThrowsException_WhenStockIsInsufficient() {
        // Arrange
        when(artworkRepository.findById(5L)).thenReturn(Optional.of(sampleArtwork));

        // Act & Assert
        GeneralException ex = assertThrows(GeneralException.class, () -> {
            artworkService.decrementQuantity(5L, 15);
        });

        assertTrue(ex.getMessage().contains("Insufficient stock"));
        verify(artworkRepository, never()).save(any());
    }

    @Test
    void changeArtworkStatus_ModifiesAndReturnsCorrectDto() {
        // Arrange
        when(artworkRepository.findById(5L)).thenReturn(Optional.of(sampleArtwork));
        when(artworkRepository.save(any(Artwork.class))).thenReturn(sampleArtwork);
        
        ArtworkResponseDto responseDto = new ArtworkResponseDto();
        responseDto.setStatus(ArtworkStatus.APPROVED);
        when(modelMapper.map(sampleArtwork, ArtworkResponseDto.class)).thenReturn(responseDto);

        // Act
        ArtworkResponseDto result = artworkService.changeArtworkStatus(5L, ArtworkStatus.APPROVED);

        // Assert
        assertEquals(ArtworkStatus.APPROVED, result.getStatus());
        assertEquals(ArtworkStatus.APPROVED, sampleArtwork.getStatus());
        verify(artworkRepository, times(1)).save(sampleArtwork);
    }
    
    @Test
    void deleteArtwork_Succeeds_WhenArtworkExists() {
        // Arrange
        when(artworkRepository.findById(5L)).thenReturn(Optional.of(sampleArtwork));
        doNothing().when(artworkRepository).deleteById(5L);

        // Act
        artworkService.deleteArtwork(5L);

        // Assert
        verify(artworkRepository, times(1)).deleteById(5L);
    }
}
