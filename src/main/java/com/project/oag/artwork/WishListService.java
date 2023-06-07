package com.project.oag.artwork;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.oag.artwork.WishList;
import com.project.oag.artwork.WishListRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WishListService {
    private final WishListRepository wishListRepository;
    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }
    public void createWishlist(WishList wishList) {
        wishListRepository.save(wishList);
    }
    public List<WishList> readWishList(Long user_id) {
        return wishListRepository.findAllByUserIdOrderByCreatedDateDesc(user_id);
    }

    public boolean deleteWishList(Integer wishlistId) {
        Optional<WishList> optionalWishList = wishListRepository.findById(wishlistId);
        if (optionalWishList.isPresent()) {
            WishList wishList = optionalWishList.get();
            wishListRepository.delete(wishList);
            return true;
        }
        return false;
    }

}
