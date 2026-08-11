package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderResponseDto;
import com.project.oag.app.entity.Order;
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
    public PaymentResponse checkout(HttpServletRequest request, OrderRequestDto orderRequestDto) {
        OrderResponseDto dto = orderService.createOrder(request, orderRequestDto);
        Order order = orderRepository.findById(dto.getId()).orElseThrow();

        PaymentResponse response = chapaService.initiatePayment(order);

        order.setPaymentLog(response.getPaymentLog());
        orderRepository.save(order);

        return response;
    }
}
