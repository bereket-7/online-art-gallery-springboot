package com.project.oag.app.service;

import com.project.oag.app.model.User;
import com.project.oag.app.model.WishList;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.repository.WishListRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

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

    public ResponseEntity<GenericResponse> saveWishlist(HttpServletRequest request,Long artworkId) {

        try {
            Long userId = getUserId(request);
            WishList wishlist = new WishList();
            wishlist.setUserId(userId);
            wishlist.setArtworkId(artworkId);
            val response = wishListRepository.save(wishlist);
            return prepareResponse(HttpStatus.OK,"successfully add to wishlist",response);
        } catch (Exception e) {
            throw new GeneralException("failed to save wishlist");
        }
    }
    public ResponseEntity<GenericResponse> deleteWishlist(HttpServletRequest request,Long id) {
        Long userId = getUserId(request);
        try {
            wishListRepository.deleteByIdAndUserId(id,userId);
            return prepareResponse(HttpStatus.OK, "Successfully deleted wishlist", null);
        } catch (Exception e) {
            throw new GeneralException("failed to delete wishlist");
        }
    }

    public ResponseEntity<GenericResponse> getUserWishlist(HttpServletRequest request) {
        Long userId = getUserId(request);
        try {
            val response = wishListRepository.findByUserId(userId);
            List<WishList> wishlists = response.stream().map((element) -> modelMapper.map(element, WishList.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Successfully retrieved wishlist", wishlists);
        } catch (Exception e) {
            throw new GeneralException("failed to find wishlist");
        }
    }

    private Long getUserId(HttpServletRequest request) {
        return getUserByUsername(getLoggedInUserName(request)).getId();
    }

    private User getUserByUsername(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Username/email: " + email));
    }

}
