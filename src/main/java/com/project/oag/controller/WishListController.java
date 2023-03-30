package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.common.ApiResponse;
import com.project.oag.entity.Artwork;
import com.project.oag.entity.User;
import com.project.oag.entity.WishList;
import com.project.oag.service.AuthenticationService;
import com.project.oag.service.WishListService;


@RestController
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired
    private WishListService wishListService;

    @Autowired
    private AuthenticationService authenticationService;

   /* @GetMapping("/{token}")
    public ResponseEntity<List<ArtworkDto>> getWishList(@PathVariable("token") String token) {
            int user_id = authenticationService.getUser(token).getId();
            List<WishList> body = wishListService.readWishList(user_id);
            List<ArtworkDto> artwork = new ArrayList<ArtworkDto>();
            for (WishList wishList : body) {
                    artwork.add(artworkService.getDtoFromArtwork(wishList.getArtwork()));
            }

            return new ResponseEntity<List<ArtworkDto>>(artwork, HttpStatus.OK);
    }*/

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(@RequestBody Artwork artwork, @RequestParam("token") String token) {
            authenticationService.authenticate(token);
            User user = authenticationService.getUser(token);
            WishList wishList = new WishList(user, artwork);
            wishListService.createWishlist(wishList);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Add to wishlist"), HttpStatus.CREATED);

    }

}
