package com.project.oag.app.service;

import com.project.oag.app.entity.Artwork;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ArtworkRepository artworkRepository;
    @Mock
    private ArtworkService artworkService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartService cartService;

    private Artwork sampleArtwork;

    @BeforeEach
    void setUp() {
        sampleArtwork = new Artwork();
        sampleArtwork.setId(100L);
        sampleArtwork.setPrice(new BigDecimal("150.0"));
        sampleArtwork.setQuantity(5);
    }

    @Test
    void decrementQuantityForArtwork_DelegatesToArtworkService() {
        cartService.decrementQuantityForArtwork(100L, 2);
        verify(artworkService, times(1)).decrementQuantity(100L, 2);
    }
}
