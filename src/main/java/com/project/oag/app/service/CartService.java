package com.project.oag.app.service;

import com.project.oag.app.dto.CartDto;
import com.project.oag.app.entity.Cart;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ArtworkRepository artworkRepository;
    private final UserRepository userRepository;
    private final ArtworkService artworkService;
    private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, ArtworkRepository artworkRepository,
                       UserRepository userRepository, ArtworkService artworkService,
                       ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.artworkRepository = artworkRepository;
        this.userRepository = userRepository;
        this.artworkService = artworkService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public CartDto addToCart(HttpServletRequest request, Long artworkId, int quantity) {
        val user = getUserByUsername(getLoggedInUserName(request));
        val artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));

        // Stock validation — prevent overselling
        int availableQty = artwork.getQuantity() == null ? 0 : artwork.getQuantity();
        if (availableQty < quantity) {
            throw new GeneralException(
                    "Insufficient stock. Requested: " + quantity + ", Available: " + availableQty);
        }

        Cart cart = new Cart();
        cart.setArtwork(artwork);
        cart.setQuantity(quantity);
        cart.setUser(user);
        user.addCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);
        log.info(LOG_PREFIX, "Added to cart", "artworkId=" + artworkId + " qty=" + quantity);
        return modelMapper.map(cart, CartDto.class);
    }

    public List<CartDto> getCarts(HttpServletRequest request) {
        Long userId = getUserByUsername(getLoggedInUserName(request)).getId();
        return cartRepository.findByUserId(userId).stream()
                .map(c -> modelMapper.map(c, CartDto.class))
                .collect(Collectors.toList());
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

    /**
     * Called by OrderService at checkout: decrements artwork stock, then removes cart items.
     * Must run within an existing @Transactional context (called from OrderService.createOrder).
     */
    @Transactional
    public void clearCartForCheckout(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        cartItems.forEach(item ->
                artworkService.decrementQuantity(item.getArtwork().getId(), item.getQuantity())
        );
        cartRepository.deleteByUserId(userId);
        log.info(LOG_PREFIX, "Cart cleared after checkout", "userId=" + userId);
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

