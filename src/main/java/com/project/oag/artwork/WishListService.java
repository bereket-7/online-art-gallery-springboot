package com.project.oag.artwork;


import com.project.oag.user.User;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class WishListService {
    private final WishListRepository wishListRepository;
    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }
    public void saveWishlist(WishList wishlist) {
        wishListRepository.save(wishlist);
    }
    public void deleteWishlist(WishList wishlist) {
        wishListRepository.delete(wishlist);
    }
    public WishList findById(Integer wishlistId) {
        return wishListRepository.findById(wishlistId)
                .orElse(null);
    }

    public List<WishList> getUserWishlist(User user) {
        return wishListRepository.findByUser(user);
    }

}
