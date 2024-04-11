package com.project.oag.app.controller;

import com.project.oag.app.dto.CartDTO;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.app.service.CartService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return cartService.addToCart(request,artworkId, quantity);
    }

//    @GetMapping
//    public ResponseEntity<List<CartDTO>> getCarts(@AuthenticationPrincipal UserDetails userDetails) {
//        String email = userDetails.getUsername();
//        List<Cart> carts = cartService.getCartsByEmail(email);
//
//        List<CartDTO> cartDTOs = carts.stream()
//                .map(cart -> new CartDTO(cart.getId(), cart.getArtwork().getArtworkName(), cart.getQuantity()))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(cartDTOs);
//    }
//    @GetMapping
//    public ResponseEntity<List<CartDTO>> getCarts(@AuthenticationPrincipal UserDetails userDetails) {
//        String email = userDetails.getUsername();
//        List<Cart> carts = cartService.getCartsByEmail(email);
//        System.out.println("Cart size: " + carts.size());
//
//        List<CartDTO> cartDTOs = new ArrayList<>();
////                .map(cart -> new CartDTO(
////                        cart.getId(),
////                        cart.getArtwork().getArtworkName(), // Populate the artwork details
////                        cart.getQuantity()
////                ))
////                .collect(Collectors.toList());
//        return ResponseEntity.ok(cartDTOs);
//    }


    @GetMapping
    public ResponseEntity<List<CartDTO>> getCartsByEmail(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<CartDTO> cartDTOs = cartService.getCartsByEmail(email);
        return ResponseEntity.ok(cartDTOs);
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
    public ResponseEntity<Integer> calculateTotalPrice(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        int totalPrice = cartService.calculateTotalPrice(username);
        return ResponseEntity.ok(totalPrice);
    }
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        cartService.clearCart(username);
        return ResponseEntity.ok("Cart cleared successfully.");
    }
}


