package com.project.oag.app.service;

import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.WishList;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.repository.WishListRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
public class WishListService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final WishListRepository wishListRepository;
    private final ArtworkRepository artworkRepository;

    public WishListService(UserRepository userRepository, ModelMapper modelMapper,
                           WishListRepository wishListRepository, ArtworkRepository artworkRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.wishListRepository = wishListRepository;
        this.artworkRepository = artworkRepository;
    }

    public WishList saveWishlist(HttpServletRequest request, Long artworkId) {
        User user = getUserByUsername(getLoggedInUserName(request));
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Artwork not found"));
        WishList wishlist = new WishList();
        wishlist.setUser(user);
        wishlist.setArtwork(artwork);
        return wishListRepository.save(wishlist);
    }

    public void deleteWishlist(HttpServletRequest request, Long id) {
        Long userId = getUserId(request);
        wishListRepository.deleteByIdAndUserId(id, userId);
    }

    public List<WishList> getUserWishlist(HttpServletRequest request) {
        Long userId = getUserId(request);
        val response = wishListRepository.findByUserId(userId);
        return response.stream().map((element) -> modelMapper.map(element, WishList.class))
                .collect(Collectors.toList());
    }

    private Long getUserId(HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }
}
