package com.project.oag.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.AddToCartDto;
import com.project.oag.controller.dto.CartDto;
import com.project.oag.controller.dto.CartItemDto;
import com.project.oag.artwork.Artwork;
import com.project.oag.entity.Cart;
import com.project.oag.entity.User;
import com.project.oag.exceptions.CartItemNotExistException;
import com.project.oag.repository.CartRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
    private CartRepository cartRepository;
    
    public CartServiceImpl(CartRepository cartRepository) {
		super();
		this.cartRepository = cartRepository;
    }

    @Override
    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        for (Cart cart:cartList){
            CartItemDto cartItemDto = getDtoFromCart(cart);
            cartItems.add(cartItemDto);
        }
        double totalCost = 0;
        for (CartItemDto cartItemDto :cartItems){
            totalCost += (cartItemDto.getArtwork().getPrice()* cartItemDto.getQuantity());
        }
        return new CartDto(cartItems,totalCost);
    }
    

    public CartItemDto getDtoFromCart(Cart cart) {
        return new CartItemDto(cart);
    }
    
    @Override
    public void deleteCartItems(long userId) {
        cartRepository.deleteAll();
    }

    @Override
    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }

	@Override
	public void deleteCartItem(long id, Long userId) throws CartItemNotExistException {
	      if (!cartRepository.existsById(id))
	            throw new CartItemNotExistException("Cart id is invalid : " + id);
	        cartRepository.deleteById(id);	
	}

	@Override
	public void addToCart(AddToCartDto addToCartDto, Optional<Artwork> artwork, User user) {
		  Cart cart = new Cart(artwork, addToCartDto.getQuantity(), user);
	        cartRepository.save(cart);
		
	}

	@Override
	public void updateCartItem(AddToCartDto cartDto, User user, Optional<Artwork> artwork) {
        Cart cart = cartRepository.getOne(cartDto.getId());
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
		
	}
}
