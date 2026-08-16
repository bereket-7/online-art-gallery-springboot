package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderResponseDto;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.entity.Artwork;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.OrderItem;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.CertificateOfAuthenticityRepository;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.ShipmentRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.BadRequestException;
import com.project.oag.utils.RequestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

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
    @Mock
    private CoaService coaService;
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private CertificateOfAuthenticityRepository certificateRepository;

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
        testOrder.setUser(testUser);
        testOrder.setItems(new ArrayList<>());
    }

    @Test
    void createOrder_ThrowsException_IfCartEmpty() {
        OrderRequestDto dto = new OrderRequestDto();
        try (MockedStatic<RequestUtils> mocked = mockStatic(RequestUtils.class)) {
            mocked.when(() -> RequestUtils.getLoggedInUserName(any())).thenReturn("test@ex.com");
            when(userRepository.findByEmailIgnoreCase("test@ex.com")).thenReturn(Optional.of(testUser));
            when(cartRepository.calculateTotalPriceByUserId(1L)).thenReturn(BigDecimal.ZERO);

            BadRequestException ex = assertThrows(BadRequestException.class, () -> orderService.createOrder(request, dto));
            assertTrue(ex.getMessage().contains("Cannot place an order with an empty cart"));
            verify(orderRepository, never()).save(any(Order.class));
        }
    }

    @Test
    void updateOrderStatus_UpdatesSuccessfully() {
        when(orderRepository.findById(10L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        when(shipmentRepository.findByOrderId(10L)).thenReturn(Optional.empty());
        when(certificateRepository.findByOrderItemOrderId(10L)).thenReturn(List.of());

        OrderResponseDto response = orderService.updateOrderStatus(10L, OrderStatus.CONFIRMED);

        assertNotNull(response);
        assertEquals(OrderStatus.CONFIRMED, testOrder.getStatus());
        verify(notificationService, times(1)).sendUserNotification(any(), any(), any());
    }

    @Test
    void fulfillOrderAfterPayment_IsIdempotent() {
        testOrder.setFulfilled(true);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(testOrder));

        orderService.fulfillOrderAfterPayment(10L);

        verify(cartService, never()).decrementQuantityForArtwork(any(), anyInt());
        verify(coaService, never()).issueForOrder(any());
    }

    @Test
    void fulfillOrderAfterPayment_DecrementsStockOnce() {
        Artwork artwork = new Artwork();
        artwork.setId(5L);
        OrderItem item = new OrderItem();
        item.setArtwork(artwork);
        item.setQuantity(2);
        testOrder.getItems().add(item);
        testOrder.setFulfilled(false);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(testOrder)).thenReturn(testOrder);

        orderService.fulfillOrderAfterPayment(10L);

        assertTrue(testOrder.isFulfilled());
        verify(cartService, times(1)).decrementQuantityForArtwork(5L, 2);
        verify(coaService, times(1)).issueForOrder(testOrder);
    }
}
