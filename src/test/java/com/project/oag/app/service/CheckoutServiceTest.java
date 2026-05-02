package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderResponseDto;
import com.project.oag.app.service.PaymentResponse;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.PaymentLog;
import com.project.oag.app.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ChapaService chapaService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    @Test
    void checkout_ShouldCreateOrderAndInitiatePaymentSuccessfully() throws Throwable {
        // Arrange
        OrderRequestDto orderRequest = new OrderRequestDto();
        OrderResponseDto orderResponse = new OrderResponseDto();
        orderResponse.setId(1L);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setTotalAmount(new BigDecimal("5000"));

        PaymentResponse mockPaymentResponse = new PaymentResponse();
        mockPaymentResponse.setTxRef("tx-1234");
        mockPaymentResponse.setCheckOutUrl("http://checkout.url");

        when(orderService.createOrder(any(HttpServletRequest.class), any(OrderRequestDto.class)))
                .thenReturn(orderResponse);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(savedOrder));
        when(chapaService.initiatePayment(savedOrder)).thenReturn(mockPaymentResponse);

        // Act
        PaymentResponse response = checkoutService.checkout(request, orderRequest);

        // Assert
        assertNotNull(response, "Payment Response should not be null");
        assertEquals("tx-1234", response.getTxRef());
        assertEquals("http://checkout.url", response.getCheckOutUrl());

        verify(orderService, times(1)).createOrder(request, orderRequest);
        verify(orderRepository, times(1)).findById(1L);
        verify(chapaService, times(1)).initiatePayment(savedOrder);
    }
}
