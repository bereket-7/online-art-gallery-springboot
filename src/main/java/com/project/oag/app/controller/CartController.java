package com.project.oag.app.controller;

import com.project.oag.app.dto.CartRequestDto;
import com.project.oag.app.dto.QuantityRequestDto;
import com.project.oag.app.service.CartService;
import com.project.oag.common.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/cart")
@Tag(name = "Cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> addToCart(HttpServletRequest request,
                                                     @Valid @RequestBody CartRequestDto dto) {
        return prepareResponse(HttpStatus.CREATED, "Artwork added to cart",
                cartService.addToCart(request, dto.getArtworkId(), dto.getQuantity()));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> addToCartLegacy(HttpServletRequest request,
                                                           @RequestParam Long artworkId,
                                                           @RequestParam int quantity) {
        return prepareResponse(HttpStatus.CREATED, "Artwork added to cart",
                cartService.addToCart(request, artworkId, quantity));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> updateQuantity(HttpServletRequest request,
                                                          @PathVariable Long id,
                                                          @Valid @RequestBody QuantityRequestDto dto) {
        return prepareResponse(HttpStatus.OK, "Cart updated",
                cartService.updateQuantity(request, id, dto.getQuantity()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> getCarts(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Cart retrieved", cartService.getCarts(request));
    }

    @DeleteMapping("/{cartId}")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> removeFromCart(HttpServletRequest request, @PathVariable Long cartId) {
        cartService.removeFromCart(request, cartId);
        return prepareResponse(HttpStatus.OK, "Item removed from cart", null);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> clearCartCanonical(HttpServletRequest request) {
        cartService.clearCart(request);
        return prepareResponse(HttpStatus.OK, "Cart cleared", null);
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> clearCart(HttpServletRequest request) {
        cartService.clearCart(request);
        return prepareResponse(HttpStatus.OK, "Cart cleared", null);
    }

    @GetMapping("/total")
    @PreAuthorize("hasAuthority('USER_MODIFY_CART')")
    public ResponseEntity<GenericResponse> getCartTotal(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Cart total", cartService.calculateTotalPrice(request));
    }
}
