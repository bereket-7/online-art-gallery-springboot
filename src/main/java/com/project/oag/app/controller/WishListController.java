package com.project.oag.app.controller;

import java.util.Date;
import java.util.List;

import com.project.oag.app.model.Artwork;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.app.model.WishList;
import com.project.oag.app.service.WishListService;
import com.project.oag.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.project.oag.user.User;
@RestController
@RequestMapping("api/wishlist")
@CrossOrigin("http://localhost:8080/")
public class WishListController {
    @Autowired
    private WishListService wishListService;
    @Autowired
    private ArtworkService artworkService;
    @Autowired
    private CustomUserDetailsService userService;
    @PostMapping("/save")
    public ResponseEntity<String> saveWishlist(@RequestBody Artwork artwork) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        WishList wishlist = new WishList();
        wishlist.setUser(loggedInUser);
        wishlist.setArtwork(artwork);
        wishlist.setCreatedDate(new Date());
        wishListService.saveWishlist(wishlist);
        return ResponseEntity.ok("Wishlist saved successfully.");
    }
    @GetMapping
    public ResponseEntity<List<WishList>> getUserWishlist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        List<WishList> wishlist = wishListService.getUserWishlist(loggedInUser);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<String> deleteWishlist(@PathVariable Integer wishlistId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        WishList wishlist = wishListService.findById(wishlistId);
        if (wishlist == null || !wishlist.getUser().equals(loggedInUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this wishlist.");
        }
        wishListService.deleteWishlist(wishlist);
        return ResponseEntity.ok("Wishlist deleted successfully.");
    }
}
