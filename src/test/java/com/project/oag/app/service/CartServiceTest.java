package com.project.oag.app.service;

import com.project.oag.app.dto.CartDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Cart;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private CartService cartService;

    private User sampleUser;
    private Artwork sampleArtwork;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("user@example.com");

        sampleArtwork = new Artwork();
        sampleArtwork.setId(100L);
        sampleArtwork.setPrice(new BigDecimal("150.0"));
        sampleArtwork.setQuantity(5);
    }

    @Test
    void decrementQuantity_ShouldSuccessIfStockAvailable() {
        // Arrange
        Cart cartItem = new Cart();
        cartItem.setArtwork(sampleArtwork);
        cartItem.setQuantity(3);
        
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cartItem);

        // Act
        cartService.decrementQuantity(1L);

        // Assert
        assertEquals(2, cartItem.getQuantity(), "Quantity should be decremented");
        verify(cartRepository, times(1)).save(cartItem);
    }

    @Test
    void addToCart_ShouldAddNewItem_WhenNotExisting() {
        // Arrange
        CartDto dto = new CartDto();
        dto.setArtworkId(100L);
        dto.setUserId(1L);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(artworkRepository.findById(100L)).thenReturn(Optional.of(sampleArtwork));
        when(cartRepository.findCartItem(100L, 1L)).thenReturn(Optional.empty());

        Cart cartItem = new Cart();
        cartItem.setQuantity(1);
        when(cartRepository.save(any(Cart.class))).thenReturn(cartItem);

        // Act
        cartService.addToCart(dto);

        // Assert
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}
