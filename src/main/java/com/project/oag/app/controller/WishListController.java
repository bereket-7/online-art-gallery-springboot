package com.project.oag.app.controller;

import com.project.oag.app.service.WishListService;
import com.project.oag.common.GenericResponse;
import com.project.oag.app.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/wishlist")
public class WishListController {
    private final WishListService wishListService;
    private final CustomUserDetailsService userService;

    public WishListController(WishListService wishListService, CustomUserDetailsService userService) {
        this.wishListService = wishListService;
        this.userService = userService;
    }

    @PostMapping("/save/{artworkId}")
    @PreAuthorize("hasAuthority('USER_ADD_WISHLIST')")
    public ResponseEntity<GenericResponse> saveWishlist(HttpServletRequest request, @PathVariable Long artworkId) {
        return prepareResponse(HttpStatus.OK, "successfully add to wishlist", wishListService.saveWishlist(request, artworkId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_FETCH_WISHLIST')")
    public ResponseEntity<GenericResponse> getUserWishlist(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Successfully retrieved wishlist", wishListService.getUserWishlist(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE_WISHLIST')")
    public ResponseEntity<GenericResponse> deleteWishlist(HttpServletRequest request, @PathVariable Long id) {
        wishListService.deleteWishlist(request, id);
        return prepareResponse(HttpStatus.OK, "Successfully deleted wishlist", null);
    }
}
