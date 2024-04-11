package com.project.oag.app.service;

import com.project.oag.app.dto.CartDTO;
import com.project.oag.app.model.Artwork;
import com.project.oag.app.model.Cart;
import com.project.oag.app.model.User;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.security.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final ArtworkRepository artworkRepository;
	private final CustomUserDetailsService userService;
	private final UserRepository userRepository;
	private final ArtworkService artworkService;

    public CartService(CartRepository cartRepository, ArtworkRepository artworkRepository, CustomUserDetailsService userService, UserRepository userRepository, ArtworkService artworkService) {
        this.cartRepository = cartRepository;
        this.artworkRepository = artworkRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.artworkService = artworkService;
    }

    public ResponseEntity<GenericResponse> addToCart(HttpServletRequest request, Long artworkId, int quantity) {
		Long userId = getUserId(request);
		try {
			val user = userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("User not found."));
			val artwork = artworkRepository.findById(artworkId)
					.orElseThrow(() -> new ResourceNotFoundException("Artwork not found."));

			Cart cart = new Cart();
			cart.setArtwork(artwork);
			cart.setQuantity(quantity);
			user.addCart(cart);
			cartRepository.save(cart);
			userRepository.save(user);
			return prepareResponse(HttpStatus.OK, "Artwork added to cart successfully", cart);
		} catch (Exception e) {
			throw new GeneralException("failed to add artwork to cart");
		}
	}
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
	private Long getUserId(HttpServletRequest request) {
		return getUserByUsername(getLoggedInUserName(request)).getId();
	}

	private User getUserByUsername(String email) {
		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
	}
}