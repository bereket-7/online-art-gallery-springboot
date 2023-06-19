package com.project.oag.controller;

import java.math.BigDecimal;
import java.util.Optional;

import com.project.oag.artwork.ArtworkService;
import com.project.oag.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.common.ApiResponse;
import com.project.oag.controller.dto.AddToCartDto;
import com.project.oag.controller.dto.CartDto;
import com.project.oag.artwork.Artwork;
import com.project.oag.user.User;
import com.project.oag.exceptions.AuthenticationFailException;
import com.project.oag.exceptions.CartItemNotExistException;
import com.project.oag.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
@CrossOrigin("http://localhost:8080/")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long artworkId, @RequestParam int quantity) {
        String username = userDetails.getUsername();
        cartService.addToCart(username, artworkId, quantity);
        return ResponseEntity.ok("Item added to cart successfully.");
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<String> removeFromCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long cartId) {
        String username = userDetails.getUsername();
        cartService.removeFromCart(username, cartId);
        return ResponseEntity.ok("Item removed from cart successfully.");
    }

    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateCartQuantity(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long cartId, @RequestParam int quantity) {
        String username = userDetails.getUsername();
        cartService.updateCartQuantity(username, cartId, quantity);
        return ResponseEntity.ok("Cart quantity updated successfully.");
    }

    @GetMapping("/totalPrice")
    public ResponseEntity<BigDecimal> calculateTotalPrice(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        BigDecimal totalPrice = cartService.calculateTotalPrice(username);
        return ResponseEntity.ok(totalPrice);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        cartService.clearCart(username);
        return ResponseEntity.ok("Cart cleared successfully.");
    }

    
}


