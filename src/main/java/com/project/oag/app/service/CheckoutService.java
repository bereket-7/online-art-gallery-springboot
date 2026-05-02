package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderResponseDto;
import com.project.oag.app.dto.PaymentResponse;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.PaymentLog;
import com.project.oag.app.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CheckoutService {

    private final OrderService orderService;
    private final ChapaService chapaService;
    private final OrderRepository orderRepository;

    public CheckoutService(OrderService orderService, ChapaService chapaService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.chapaService = chapaService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentResponse checkout(HttpServletRequest request, OrderRequestDto orderRequestDto) throws Throwable {
        // Step 1 & 2 & 3: OrderService encapsulates validating the cart, creating PENDING Order and clearing the Cart.
        // Wait, OrderService.createOrder returns OrderResponseDto. 
        // We actually need the persistent Order entity to attach the PaymentLog to it.
        OrderResponseDto dto = orderService.createOrder(request, orderRequestDto);
        Order order = orderRepository.findById(dto.getId()).orElseThrow();

        // Step 4: Initiate Chapa payment natively 
        PaymentResponse response = chapaService.initiatePayment(order);

        // Step 5: Assign the token txRef natively into the Order. (Order currently has secretCode natively. We could use it or paymentLog)
        // Since ChapaService inside initiatePayment generated a PaymentLog internally with txRef, we can link it up
        return response;
    }
}
