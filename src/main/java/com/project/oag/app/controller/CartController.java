package com.project.oag.app.controller;

import com.project.oag.app.service.ArtworkService;
import com.project.oag.app.service.CartService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
public class CartController {
    private final CartService cartService;
    private final ArtworkService artworkService;

    public CartController(CartService cartService, ArtworkService artworkService) {
        this.cartService = cartService;
        this.artworkService = artworkService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> addToCart(HttpServletRequest request, Long artworkId, int quantity) {
        return cartService.addToCart(request, artworkId, quantity);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> getCartsByEmail(HttpServletRequest request) {
        return cartService.getCarts(request);
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> removeFromCart(HttpServletRequest request, @PathVariable Long cartId) {
        return cartService.removeFromCart(request, cartId);
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> clearCart(HttpServletRequest request) {
        return cartService.clearCart(request);
    }

}


