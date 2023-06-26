package com.project.oag.shopping.cart;

import com.project.oag.artwork.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/cart")
@CrossOrigin("http://localhost:8080/")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ArtworkService artworkService;

//    @PostMapping("/add")
//    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long artworkId, @RequestParam int quantity) {
//        String email = userDetails.getUsername();
//        cartService.addToCart(email, artworkId, quantity);
//        return ResponseEntity.ok("Item added to cart successfully.");
//    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, Object> request) {
        String email = userDetails.getUsername();
        Long artworkId = Long.parseLong(request.get("artworkId").toString());
        int quantity = Integer.parseInt(request.get("quantity").toString());
        cartService.addToCart(email, artworkId, quantity);
        return ResponseEntity.ok("Item added to cart successfully.");
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


