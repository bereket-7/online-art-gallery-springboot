package com.project.oag.service;

import java.util.Optional;

import com.project.oag.controller.dto.AddToCartDto;
import com.project.oag.controller.dto.CartDto;
import com.project.oag.artwork.Artwork;
import com.project.oag.user.User;
import com.project.oag.exceptions.CartItemNotExistException;

public interface CartService {

	void addToCart(AddToCartDto addToCartDto, Optional<Artwork> artwork, User user);

	CartDto listCartItems(User user);

	void deleteCartItems(long userId);

	void deleteUserCartItems(User user);

	void updateCartItem(AddToCartDto cartDto, User user, Optional<Artwork> artwork);

	void deleteCartItem(long id, Long userId) throws CartItemNotExistException;

}
