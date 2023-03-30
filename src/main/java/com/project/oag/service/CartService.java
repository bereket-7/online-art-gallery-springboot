package com.project.oag.service;

import com.project.oag.controller.dto.AddToCartDto;
import com.project.oag.controller.dto.CartDto;
import com.project.oag.controller.dto.CartItemDto;
import com.project.oag.entity.Artwork;
import com.project.oag.entity.Cart;
import com.project.oag.entity.User;
import com.project.oag.exceptions.CartItemNotExistException;

public interface CartService {

	void addToCart(AddToCartDto addToCartDto, Artwork artwork, User user);

	CartItemDto getDtoFromCart(Cart cart);

	void deleteCartItem(int id, int userId) throws CartItemNotExistException;

	void deleteCartItems(int userId);

	void deleteUserCartItems(User user);

	CartDto listCartItems(User user);

	void updateCartItem(AddToCartDto cartDto, User user, Artwork artwork);

	void deleteCartItem(int itemID, Long userId);

}
