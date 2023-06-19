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
//    @Override
//    public CartDto listCartItems(User user) {
//        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
//        List<CartItemDto> cartItems = new ArrayList<>();
//        for (Cart cart:cartList){
//            CartItemDto cartItemDto = getDtoFromCart(cart);
//            cartItems.add(cartItemDto);
//        }
//        double totalCost = 0;
//        for (CartItemDto cartItemDto :cartItems){
//            totalCost += (cartItemDto.getArtwork().getPrice()* cartItemDto.getQuantity());
//        }
//        return new CartDto(cartItems,totalCost);
//    }
//
//
//    public CartItemDto getDtoFromCart(Cart cart) {
//        return new CartItemDto(cart);
//    }
//
//    @Override
//    public void deleteCartItems(long userId) {
//        cartRepository.deleteAll();
//    }
//
//    @Override
//    public void deleteUserCartItems(User user) {
//        cartRepository.deleteByUser(user);
//    }
//
//	@Override
//	public void deleteCartItem(long id, Long userId) throws CartItemNotExistException {
//	      if (!cartRepository.existsById(id))
//	            throw new CartItemNotExistException("Cart id is invalid : " + id);
//	        cartRepository.deleteById(id);
//	}
//
//	@Override
//	public void addToCart(AddToCartDto addToCartDto, Optional<Artwork> artwork, User user) {
//		  Cart cart = new Cart(artwork, addToCartDto.getQuantity(), user);
//	        cartRepository.save(cart);
//
//	}
//
//	@Override
//	public void updateCartItem(AddToCartDto cartDto, User user, Optional<Artwork> artwork) {
//        Cart cart = cartRepository.getOne(cartDto.getId());
//        cart.setQuantity(cartDto.getQuantity());
//        cart.setCreatedDate(new Date());
//        cartRepository.save(cart);
//
//	}




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
            userService.saveUser(user);
        }
    }

    public void updateCartQuantity(String username, Long cartId, int quantity) {
        User user = userService.getUserByUsername(username);

        Optional<Cart> optionalCart = user.getCarts().stream()
                .filter(cart -> cart.getId().equals(cartId))
                .findFirst();

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.setQuantity(quantity);
            userService.saveUser(user);
        }
    }

    public BigDecimal calculateTotalPrice(String username) {
        User user = userService.getUserByUsername(username);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Cart cart : user.getCarts()) {
            Artwork artwork = cart.getArtwork();
            BigDecimal artworkPrice = artwork.getPrice();
            int quantity = cart.getQuantity();

            BigDecimal cartTotalPrice = artworkPrice.multiply(BigDecimal.valueOf(quantity));
            totalPrice = totalPrice.add(cartTotalPrice);
        }

        return totalPrice;
    }

    public void clearCart(String username) {
        User user = userService.getUserByUsername(username);

        user.clearCarts();
        userService.saveUser(user);
    }
}
