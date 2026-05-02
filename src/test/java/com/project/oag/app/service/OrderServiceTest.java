package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderResponseDto;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private NotificationWebSocketService notificationService;

    @InjectMocks
    private OrderService orderService;

    private MockHttpServletRequest request;
    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@ex.com");

        testOrder = new Order();
        testOrder.setId(10L);
        testOrder.setTotalAmount(new BigDecimal("100.00"));
        testOrder.setEmail("test@ex.com");
    }

    @Test
    void createOrder_ThrowsException_IfCartEmpty() {
        // Arrange
        OrderRequestDto dto = new OrderRequestDto();
        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(testUser));
        when(cartRepository.calculateTotalPriceByUserId(1L)).thenReturn(BigDecimal.ZERO);

        // Act & Assert
        GeneralException ex = assertThrows(GeneralException.class, () -> {
            orderService.createOrder(request, dto);
        });

        assertTrue(ex.getMessage().contains("Cannot place an order with an empty cart"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_UpdatesSuccessfully() {
        // Arrange
        when(orderRepository.findById(10L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        
        OrderResponseDto mockResp = new OrderResponseDto();
        mockResp.setStatus(OrderStatus.CONFIRMED);
        when(modelMapper.map(testOrder, OrderResponseDto.class)).thenReturn(mockResp);

        // Act
        OrderResponseDto response = orderService.updateOrderStatus(10L, OrderStatus.CONFIRMED);

        // Assert
        assertNotNull(response);
        assertEquals(OrderStatus.CONFIRMED, testOrder.getStatus());
        verify(notificationService, times(1)).sendUserNotification(any(), any());
    }
}
