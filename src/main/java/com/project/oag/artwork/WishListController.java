package com.project.oag.artwork;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.oag.common.ApiResponse;
import com.project.oag.user.User;
import com.project.oag.service.AuthenticationService;
@RestController
@RequestMapping("/wishlist")
@CrossOrigin("http://localhost:8080/")
public class WishListController {
    @Autowired
    private WishListService wishListService;
    @Autowired
    private ArtworkService artworkService;
    @Autowired
    private AuthenticationService authenticationService;
    @GetMapping("/get/{token}")
    public ResponseEntity<List<ArtworkDto>> getWishList(@PathVariable("token") String token) {
            Long user_id = authenticationService.getUser(token).getId();
            List<WishList> body = wishListService.readWishList(user_id);
            List<ArtworkDto> artwork = new ArrayList<ArtworkDto>();
            for (WishList wishList : body) {
                    artwork.add(artworkService.getDtoFromArtwork(wishList.getArtwork()));
            }

            return new ResponseEntity<List<ArtworkDto>>(artwork, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(@RequestBody Artwork artwork, @RequestParam("token") String token) {
            authenticationService.authenticate(token);
            User user = authenticationService.getUser(token);
            WishList wishList = new WishList(user, artwork);
            wishListService.createWishlist(wishList);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Add to wishlist"), HttpStatus.CREATED);

    }
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<String> deleteWishList(@PathVariable Integer wishlistId) {
        boolean deleted = wishListService.deleteWishList(wishlistId);
        if (deleted) {
            return ResponseEntity.ok("WishList deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
