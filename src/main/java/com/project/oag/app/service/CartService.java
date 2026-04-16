package com.project.oag.app.service;

import com.project.oag.app.dto.CartDto;
import com.project.oag.app.entity.Cart;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, ArtworkRepository artworkRepository,
                       UserRepository userRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public CartDto addToCart(HttpServletRequest request, Long artworkId, int quantity) {
        val user = getUserByUsername(getLoggedInUserName(request));
        val artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        Cart cart = new Cart();
        cart.setArtwork(artwork);
        cart.setQuantity(quantity);
        cart.setUser(user);
        user.addCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);
        return modelMapper.map(cart, CartDto.class);
    }

    public List<CartDto> getCarts(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        return cartRepository.findByUserId(userId).stream()
                .map(c -> modelMapper.map(c, CartDto.class))
                .collect(Collectors.toList());
    }

    public void removeFromCart(HttpServletRequest request, Long cartId) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        cartRepository.deleteByUserIdAndId(userId, cartId);
    }

    public void clearCart(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        cartRepository.deleteByUserId(userId);
    }

    public BigDecimal calculateTotalPrice(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        return cartRepository.calculateTotalPriceByUserId(userId);
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
