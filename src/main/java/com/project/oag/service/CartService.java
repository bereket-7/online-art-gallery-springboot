package com.project.oag.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.project.oag.artwork.ArtworkService;
import com.project.oag.exceptions.ArtworkNotFoundException;
import com.project.oag.security.service.CustomUserDetailsService;
import com.project.oag.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.artwork.Artwork;
import com.project.oag.entity.Cart;
import com.project.oag.user.User;
import com.project.oag.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartService {
	@Autowired
    private CartRepository cartRepository;
    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ArtworkService artworkService;
    public CartService(CartRepository cartRepository) {
		super();
		this.cartRepository = cartRepository;
    }
    public void addToCart(String username, Long artworkId, int quantity) {
        User user = userRepository.findByUsername(username);
        Optional<Artwork> optionalArtwork = artworkService.getArtworkById(artworkId);
        if (optionalArtwork.isEmpty()) {
            throw new ArtworkNotFoundException("Artwork not found.");
        }
        Artwork artwork = optionalArtwork.get();

        Cart cart = new Cart();
        cart.setArtwork(artwork);
        cart.setQuantity(quantity);

        user.addCart(cart);
        userRepository.save(user);
    }

    public void removeFromCart(String username, Long cartId) {
        User user = userRepository.findByUsername(username);

        Optional<Cart> optionalCart = user.getCarts().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst();

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            user.removeCart(cart);
            userRepository.save(user);
        }
    }

    public void updateCartQuantity(String username, Long cartId, int quantity) {
        User user = userRepository.findByUsername(username);

        Optional<Cart> optionalCart = user.getCarts().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst();

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.setQuantity(quantity);
            userRepository.save(user);
        }
    }

    public int calculateTotalPrice(String username) {
        User user = userRepository.findByUsername(username);

        int totalPrice = 0;

        for (Cart cart : user.getCarts()) {
            Artwork artwork = cart.getArtwork();
            int artworkPrice = artwork.getPrice();
            int quantity = cart.getQuantity();

            int cartTotalPrice = artworkPrice * quantity;
            totalPrice += cartTotalPrice;
        }

        return totalPrice;
    }

    public void clearCart(String username) {
        User user = userRepository.findByUsername(username);
        user.clearCarts();
        userRepository.save(user);
    }
}
