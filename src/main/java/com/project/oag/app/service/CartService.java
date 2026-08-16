package com.project.oag.app.service;

import com.project.oag.app.dto.CartDto;
import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Cart;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.BadRequestException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ArtworkService artworkService;

    public CartService(CartRepository cartRepository, ArtworkRepository artworkRepository,
                       UserRepository userRepository, ArtworkService artworkService) {
        this.cartRepository = cartRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.artworkService = artworkService;
    }

    @Transactional
    public CartDto addToCart(HttpServletRequest request, Long artworkId, int quantity) {
        val user = getUserByUsername(getLoggedInUserName(request));
        val artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        Cart existing = cartRepository.findByUserIdAndArtworkId(user.getId(), artworkId).orElse(null);
        int newQty = quantity + (existing == null ? 0 : existing.getQuantity());
        assertStock(artwork, newQty);

        if (existing != null) {
            existing.setQuantity(newQty);
            Cart saved = cartRepository.save(existing);
            log.info(LOG_PREFIX, "Merged cart quantity", "artworkId=" + artworkId + " qty=" + newQty);
            return CommerceMappers.toCartDto(saved);
        }

        Cart cart = new Cart();
        cart.setArtwork(artwork);
        cart.setQuantity(quantity);
        cart.setUser(user);
        user.addCart(cart);
        cartRepository.save(cart);
        log.info(LOG_PREFIX, "Added to cart", "artworkId=" + artworkId + " qty=" + quantity);
        return CommerceMappers.toCartDto(cart);
    }

    @Transactional
    public CartDto updateQuantity(HttpServletRequest request, Long cartId, int quantity) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        Cart cart = cartRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        assertStock(cart.getArtwork(), quantity);
        cart.setQuantity(quantity);
        return CommerceMappers.toCartDto(cartRepository.save(cart));
    }

    public List<CartDto> getCarts(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        return cartRepository.findByUserId(userId).stream()
                .map(CommerceMappers::toCartDto)
                .toList();
    }

    @Transactional
    public void removeFromCart(HttpServletRequest request, Long cartId) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        cartRepository.deleteByUserIdAndId(userId, cartId);
    }

    @Transactional
    public void clearCart(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        cartRepository.deleteByUserId(userId);
    }

    public void decrementQuantityForArtwork(Long artworkId, int qty) {
        artworkService.decrementQuantity(artworkId, qty);
    }

    public BigDecimal calculateTotalPrice(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        BigDecimal total = cartRepository.calculateTotalPriceByUserId(userId);
        return total == null ? BigDecimal.ZERO : total;
    }

    private void assertStock(Artwork artwork, int quantity) {
        int availableQty = artwork.getQuantity() == null ? 0 : artwork.getQuantity();
        if (availableQty < quantity) {
            throw new BadRequestException(
                    "Insufficient stock. Requested: " + quantity + ", Available: " + availableQty);
        }
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
