package com.project.oag.app.service;

import com.project.oag.app.dto.CartDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final ArtworkRepository artworkRepository;
	private final CustomUserDetailsService userService;
	private final UserRepository userRepository;
	private final ArtworkService artworkService;
	private final ModelMapper modelMapper;

    public CartService(CartRepository cartRepository, ArtworkRepository artworkRepository, CustomUserDetailsService userService, UserRepository userRepository, ArtworkService artworkService, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.artworkRepository = artworkRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.artworkService = artworkService;
        this.modelMapper = modelMapper;
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
	public ResponseEntity<GenericResponse> getCarts(HttpServletRequest request) {
		Long userId = getUserId(request);
		try {
			List<Cart> results= cartRepository.findByUserId(userId);
			List<CartDto> response = results.stream().map((element) ->
					modelMapper.map(element, CartDto.class))
					.collect(Collectors.toList());
			return prepareResponse(HttpStatus.OK, "Successfully retrieved carts", response);
		} catch (Exception e) {
			throw new GeneralException("Failed to retrieve cart");
		}
	}
	public ResponseEntity<GenericResponse> removeFromCart(HttpServletRequest request, Long cartId) {
		try {
			Long userId = getUserId(request);
			cartRepository.deleteByUserIdAndId(userId, cartId);
			return prepareResponse(HttpStatus.OK, "Successfully removed from cart", null);
		} catch (Exception e) {
			throw new GeneralException("Failed to remove cart item");
		}
	}

	public ResponseEntity<GenericResponse> clearCart(HttpServletRequest request) {
		Long userId = getUserId(request);
		try {
			cartRepository.deleteByUserId(userId);
			return prepareResponse(HttpStatus.OK, "Successfully cleared cart", null);
		} catch (Exception e) {
			throw new GeneralException("Error deleting cart");
		}
	}
	private Long getUserId(HttpServletRequest request) {
		return getUserByUsername(getLoggedInUserName(request)).getId();
	}

	private User getUserByUsername(String email) {
		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
	}
}