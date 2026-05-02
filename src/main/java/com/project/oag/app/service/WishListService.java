package com.project.oag.app.service;

import com.project.oag.app.entity.User;
import com.project.oag.app.entity.WishList;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.repository.WishListRepository;
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

    public WishListService(UserRepository userRepository, ModelMapper modelMapper, WishListRepository wishListRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.wishListRepository = wishListRepository;
    }

    public WishList saveWishlist(HttpServletRequest request, Long artworkId) {
        Long userId = getUserId(request);
        WishList wishlist = new WishList();
        //wishlist.setUserId(userId);
       // wishlist.setArtworkId(artworkId);
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
