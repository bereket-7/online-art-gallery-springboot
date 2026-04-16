package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.entity.Order;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final JavaMailSender javaMailSender;

    public OrderService(ModelMapper modelMapper, OrderRepository orderRepository, JavaMailSender javaMailSender) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.javaMailSender = javaMailSender;
    }

    public Order createOrder(OrderRequestDto dto) {
        val order = modelMapper.map(dto, Order.class);
        order.setSecretCode(generateSecretCode());
        val saved = orderRepository.save(order);
        sendOrderConfirmationEmail(saved);
        return saved;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrderById(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.deleteById(id);
    }

    @Async
    protected void sendOrderConfirmationEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getEmail());
        message.setSubject("Order Confirmation");
        message.setText("Thank you for your order!\n\n" +
                "First Name: " + order.getFirstname() + "\n" +
                "Last Name: " + order.getLastname() + "\n" +
                "Email: " + order.getEmail() + "\n" +
                "Phone: " + order.getPhone() + "\n" +
                "Order Date: " + order.getOrderDate() + "\n" +
                "Secret Code: " + order.getSecretCode());
        javaMailSender.send(message);
    }

    private String generateSecretCode() {
        int code = 100000 + new Random().nextInt(900000);
        return String.valueOf(code);
    }
}
