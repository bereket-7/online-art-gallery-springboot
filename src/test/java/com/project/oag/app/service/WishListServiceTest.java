package com.project.oag.app.service;

import com.project.oag.app.entity.User;
import com.project.oag.app.entity.WishList;
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
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
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
    private ModelMapper modelMapper;

    @Mock
    private WishListRepository wishListRepository;

    @InjectMocks
    private WishListService wishListService;

    private MockHttpServletRequest request;
    private User testUser;
    private WishList mockWishList;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        
        testUser = new User();
        testUser.setId(30L);
        testUser.setEmail("buyer@art.com");

        mockWishList = new WishList();
        mockWishList.setId(70L);
    }

    @Test
    void getUserWishlist_ResolvesAndReturnsCorrectList() {
        // Arrange
        try (MockedStatic<RequestUtils> mockedRequestUtils = mockStatic(RequestUtils.class)) {
            mockedRequestUtils.when(() -> RequestUtils.getLoggedInUserName(any())).thenReturn("buyer@art.com");
            
            when(userRepository.findByEmailIgnoreCase("buyer@art.com")).thenReturn(Optional.of(testUser));
            when(wishListRepository.findByUserId(30L)).thenReturn(Collections.singletonList(mockWishList));
            when(modelMapper.map(mockWishList, WishList.class)).thenReturn(mockWishList);

            // Act
            List<WishList> results = wishListService.getUserWishlist(request);

            // Assert
            assertEquals(1, results.size());
            assertEquals(70L, results.get(0).getId());
            
            verify(wishListRepository, times(1)).findByUserId(30L);
        }
    }

    @Test
    void deleteWishlist_TriggerDeletionProperlyAttachedToUser() {
        // Arrange
        try (MockedStatic<RequestUtils> mockedRequestUtils = mockStatic(RequestUtils.class)) {
            mockedRequestUtils.when(() -> RequestUtils.getLoggedInUserName(any())).thenReturn("buyer@art.com");
            when(userRepository.findByEmailIgnoreCase("buyer@art.com")).thenReturn(Optional.of(testUser));
            
            doNothing().when(wishListRepository).deleteByIdAndUserId(70L, 30L);

            // Act
            wishListService.deleteWishlist(request, 70L);

            // Assert
            verify(wishListRepository, times(1)).deleteByIdAndUserId(70L, 30L);
        }
    }
}
