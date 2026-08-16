package com.project.oag.app.service;

import com.project.oag.app.dto.CartDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Cart;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.utils.RequestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    private CartService cartService;

    private Artwork sampleArtwork;
    private User user;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        user = new User();
        user.setId(1L);
        user.setEmail("buyer@art.com");
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

    @Test
    void addToCart_MergesQuantity_WhenSameArtworkAlreadyInCart() {
        Cart existing = new Cart();
        existing.setId(9L);
        existing.setUser(user);
        existing.setArtwork(sampleArtwork);
        existing.setQuantity(1);

        try (MockedStatic<RequestUtils> mocked = mockStatic(RequestUtils.class)) {
            mocked.when(() -> RequestUtils.getLoggedInUserName(any())).thenReturn("buyer@art.com");
            when(userRepository.findByEmailIgnoreCase("buyer@art.com")).thenReturn(Optional.of(user));
            when(artworkRepository.findById(100L)).thenReturn(Optional.of(sampleArtwork));
            when(cartRepository.findByUserIdAndArtworkId(1L, 100L)).thenReturn(Optional.of(existing));
            when(cartRepository.save(existing)).thenReturn(existing);

            CartDto dto = cartService.addToCart(request, 100L, 2);

            assertEquals(3, dto.getQuantity());
            verify(cartRepository, times(1)).save(existing);
        }
    }
}
