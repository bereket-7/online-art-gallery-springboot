package com.project.oag.app.service;

import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.dto.WishlistCheckDto;
import com.project.oag.app.dto.WishlistItemDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.WishList;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.repository.WishListRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
public class WishListService {
    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;
    private final ArtworkRepository artworkRepository;

    public WishListService(UserRepository userRepository,
                           WishListRepository wishListRepository, ArtworkRepository artworkRepository) {
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
        this.artworkRepository = artworkRepository;
    }

    @Transactional
    public WishlistItemDto saveWishlist(HttpServletRequest request, Long artworkId) {
        User user = getUserByUsername(getLoggedInUserName(request));
        return wishListRepository.findByUserIdAndArtworkId(user.getId(), artworkId)
                .map(CommerceMappers::toWishlistDto)
                .orElseGet(() -> {
                    Artwork artwork = artworkRepository.findById(artworkId)
                            .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
                    WishList wishlist = new WishList();
                    wishlist.setUser(user);
                    wishlist.setArtwork(artwork);
                    return CommerceMappers.toWishlistDto(wishListRepository.save(wishlist));
                });
    }

    public void deleteWishlist(HttpServletRequest request, Long id) {
        Long userId = getUserId(request);
        wishListRepository.deleteByIdAndUserId(id, userId);
    }

    public List<WishlistItemDto> getUserWishlist(HttpServletRequest request) {
        Long userId = getUserId(request);
        return wishListRepository.findByUserId(userId).stream()
                .map(CommerceMappers::toWishlistDto)
                .toList();
    }

    public WishlistCheckDto checkWishlist(HttpServletRequest request, Long artworkId) {
        Long userId = getUserId(request);
        return wishListRepository.findByUserIdAndArtworkId(userId, artworkId)
                .map(w -> new WishlistCheckDto(true, w.getId()))
                .orElse(new WishlistCheckDto(false, null));
    }

    private Long getUserId(HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }
}
