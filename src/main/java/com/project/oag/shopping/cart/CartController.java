package com.project.oag.shopping.cart;

import com.project.oag.artwork.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/cart")
@CrossOrigin("http://localhost:8080/")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ArtworkService artworkService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long artworkId, @RequestParam int quantity) {
        String email = userDetails.getUsername(); // Use email instead of username
        cartService.addToCart(email, artworkId, quantity);
        return ResponseEntity.ok("Item added to cart successfully.");
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getCarts(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<Cart> carts = cartService.getCartsByEmail(email);

        List<CartDTO> cartDTOs = carts.stream()
                .map(cart -> new CartDTO(cart.getId(), cart.getArtwork().getArtworkName(), cart.getQuantity()))
                .collect(Collectors.toList());
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

