package com.project.oag.shopping.cart;

import java.util.List;
import java.util.Optional;

import com.project.oag.artwork.ArtworkService;
import com.project.oag.exceptions.ArtworkNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.security.service.CustomUserDetailsService;
import com.project.oag.shopping.cart.Cart;
import com.project.oag.shopping.cart.CartRepository;
import com.project.oag.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.artwork.Artwork;
import com.project.oag.user.User;
import org.springframework.transaction.annotation.Transactional;

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
	public void addToCart(String email, Long artworkId, int quantity) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
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
		} else {
			throw new UserNotFoundException("User not found.");
		}
	}
//	public List<Cart> getCartsByEmail(String email) {
//		Optional<User> optionalUser = userRepository.findByEmail(email);
//		if (optionalUser.isPresent()) {
//			User user = optionalUser.get();
//			return user.getCarts();
//		} else {
//			throw new UserNotFoundException("User not found.");
//		}
//	}
	@Transactional(readOnly = true)
	public List<Cart> getCartsByEmail(String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			List<Cart> carts = user.getCarts();
			// Initialize the associated artwork objects
			carts.forEach(cart -> {
				cart.getArtwork().getArtworkName();
			});
			return carts;
		} else {
			throw new UserNotFoundException("User not found.");
		}
	}
	public void removeFromCart(String email, Long cartId) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();

		Optional<Cart> optionalCart = user.getCarts().stream()
				.filter(cart -> cart.getId().equals(cartId))
				.findFirst();

		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();
			user.removeCart(cart);
			userRepository.save(user);
		}
		}else {
			throw new UserNotFoundException("User not found.");
		}
	}

	public void updateCartQuantity(String email, Long cartId, int quantity) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			Optional<Cart> optionalCart = user.getCarts().stream()
					.filter(cart -> cart.getId().equals(cartId))
					.findFirst();

			if (optionalCart.isPresent()) {
				Cart cart = optionalCart.get();
				cart.setQuantity(quantity);
				userRepository.save(user);
			}
		} else {
			throw new UserNotFoundException("User not found.");
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