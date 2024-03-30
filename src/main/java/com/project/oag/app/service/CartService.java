package com.project.oag.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.project.oag.app.dto.CartDTO;
import com.project.oag.app.model.Cart;
import com.project.oag.app.repository.CartRepository;
import org.springframework.transaction.annotation.Transactional;
import com.project.oag.exceptions.ArtworkNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.security.service.CustomUserDetailsService;
import com.project.oag.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.app.model.Artwork;
import com.project.oag.app.model.User;


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

//	@Transactional(readOnly = true)
//	public List<Cart> getCartsByEmail(String email) {
//		Optional<User> optionalUser = userRepository.findByEmail(email);
//		if (optionalUser.isPresent()) {
//			User user = optionalUser.get();
//			List<Cart> carts = user.getCarts();
//			carts.forEach(cart -> {
//				cart.getArtwork().getArtworkName();
//			});
//			return carts;
//		} else {
//			throw new UserNotFoundException("User not found.");
//		}
//	}

//	@Transactional(readOnly = true)
//	public List<Cart> getCartsByEmail(String email) {
//		Optional<User> optionalUser = userRepository.findByEmail(email);
//		if (optionalUser.isPresent()) {
//			User user = optionalUser.get();
//			return user.getCarts();
//		} else {
//			throw new UserNotFoundException("User not found.");
//		}
//	}
public List<CartDTO> getCartsByEmail(String email) {
	List<Object[]> results = cartRepository.getCartsWithEmailAndArtworkName(email);

	List<CartDTO> cartDTOs = new ArrayList<>();

	for (Object[] result : results) {
		System.out.println(Arrays.stream(result).toList());
		CartDTO cartDTO = new CartDTO();
		cartDTO.setId((Long) result[0]); // cart ID
		cartDTO.setQuantity((int) result[1]); // quantity
		cartDTO.setArtworkName((String) result[2]); // artworkName
		cartDTOs.add(cartDTO);
	}

	return cartDTOs;
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

	public int calculateTotalPrice(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		//User user = userRepository.findByUsername(username);
		int totalPrice = 0;
		for (Cart cart : user.get().getCarts()) {
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