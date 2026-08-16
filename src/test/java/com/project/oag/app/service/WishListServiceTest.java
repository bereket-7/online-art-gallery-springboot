package com.project.oag.app.service;

import com.project.oag.app.dto.WishlistItemDto;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.User;
import com.project.oag.app.entity.WishList;
import com.project.oag.app.repository.ArtworkRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.repository.WishListRepository;
import com.project.oag.utils.RequestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishListServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private ArtworkRepository artworkRepository;

    @InjectMocks
    private WishListService wishListService;

    private MockHttpServletRequest request;
    private User testUser;
    private WishList first;
    private WishList second;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        testUser = new User();
        testUser.setId(30L);
        testUser.setEmail("buyer@art.com");

        Artwork a1 = new Artwork();
        a1.setId(1L);
        Artwork a2 = new Artwork();
        a2.setId(2L);

        first = new WishList();
        first.setId(70L);
        first.setArtwork(a1);
        second = new WishList();
        second.setId(71L);
        second.setArtwork(a2);
    }

    @Test
    void getUserWishlist_ReturnsManyItems() {
        try (MockedStatic<RequestUtils> mockedRequestUtils = mockStatic(RequestUtils.class)) {
            mockedRequestUtils.when(() -> RequestUtils.getLoggedInUserName(any())).thenReturn("buyer@art.com");
            when(userRepository.findByEmailIgnoreCase("buyer@art.com")).thenReturn(Optional.of(testUser));
            when(wishListRepository.findByUserId(30L)).thenReturn(List.of(first, second));

            List<WishlistItemDto> results = wishListService.getUserWishlist(request);

            assertEquals(2, results.size());
            assertEquals(70L, results.get(0).getId());
            assertEquals(71L, results.get(1).getId());
        }
    }

    @Test
    void saveWishlist_IsIdempotent_WhenAlreadyPresent() {
        try (MockedStatic<RequestUtils> mockedRequestUtils = mockStatic(RequestUtils.class)) {
            mockedRequestUtils.when(() -> RequestUtils.getLoggedInUserName(any())).thenReturn("buyer@art.com");
            when(userRepository.findByEmailIgnoreCase("buyer@art.com")).thenReturn(Optional.of(testUser));
            when(wishListRepository.findByUserIdAndArtworkId(30L, 1L)).thenReturn(Optional.of(first));

            WishlistItemDto dto = wishListService.saveWishlist(request, 1L);

            assertEquals(70L, dto.getId());
            verify(wishListRepository, never()).save(any());
        }
    }

    @Test
    void deleteWishlist_TriggerDeletionProperlyAttachedToUser() {
        try (MockedStatic<RequestUtils> mockedRequestUtils = mockStatic(RequestUtils.class)) {
            mockedRequestUtils.when(() -> RequestUtils.getLoggedInUserName(any())).thenReturn("buyer@art.com");
            when(userRepository.findByEmailIgnoreCase("buyer@art.com")).thenReturn(Optional.of(testUser));
            when(wishListRepository.deleteByIdAndUserId(70L, 30L)).thenReturn(1);

            wishListService.deleteWishlist(request, 70L);

            verify(wishListRepository, times(1)).deleteByIdAndUserId(70L, 30L);
        }
    }
}
