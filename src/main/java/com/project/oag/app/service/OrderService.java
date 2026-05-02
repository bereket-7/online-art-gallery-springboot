package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderResponseDto;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final JavaMailSender javaMailSender;
    private final NotificationWebSocketService notificationService;

    public OrderService(ModelMapper modelMapper,
                        OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartRepository cartRepository,
                        CartService cartService,
                        JavaMailSender javaMailSender,
                        NotificationWebSocketService notificationService) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.javaMailSender = javaMailSender;
        this.notificationService = notificationService;
    }

    /**
     * Creates an order for the authenticated user.
     * Computes the total from the user's active cart and clears the cart atomically.
     */
    @Transactional
    public OrderResponseDto createOrder(HttpServletRequest request, OrderRequestDto dto) {
        User user = resolveUser(request);

        // Compute total from active cart
        BigDecimal total = cartRepository.calculateTotalPriceByUserId(user.getId());
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new GeneralException("Cannot place an order with an empty cart");
        }

        Order order = modelMapper.map(dto, Order.class);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(total);
        order.setSecretCode(generateSecretCode());

        Order saved = orderRepository.save(order);
        log.info(LOG_PREFIX, "Order created", "orderId=" + saved.getId() + " userId=" + user.getId());

        // Decrement artwork quantities and clear cart transactionally
        cartService.clearCartForCheckout(user.getId());

        sendOrderConfirmationEmail(saved);
        
        notificationService.sendUserNotification(user.getEmail(), "Order #" + saved.getId() + " initialized pending payment!");
        
        return modelMapper.map(saved, OrderResponseDto.class);
    }

    /** Admin: all orders */
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderResponseDto.class))
                .collect(Collectors.toList());
    }

    /** Customer: own orders only */
    public List<OrderResponseDto> getMyOrders(HttpServletRequest request) {
        User user = resolveUser(request);
        return orderRepository.findByUserId(user.getId()).stream()
                .map(o -> modelMapper.map(o, OrderResponseDto.class))
                .collect(Collectors.toList());
    }

    /** Admin: update order status */
    @Transactional
    public OrderResponseDto updateOrderStatus(Long id, OrderStatus status) {
        val order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        
        Order saved = orderRepository.save(order);
        notificationService.sendUserNotification(saved.getEmail(), "Your order #" + saved.getId() + " is now " + status);
        
        return modelMapper.map(saved, OrderResponseDto.class);
    }

    @Transactional
    public void deleteOrderById(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.deleteById(id);
    }

    @Async
    protected void sendOrderConfirmationEmail(Order order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(order.getEmail());
            message.setSubject("Order Confirmation — Kelem OAG");
            message.setText(
                    "Thank you for your order!\n\n" +
                    "Order ID   : " + order.getId() + "\n" +
                    "Name       : " + order.getFirstname() + " " + order.getLastname() + "\n" +
                    "Total      : " + order.getTotalAmount() + "\n" +
                    "Status     : " + order.getStatus() + "\n" +
                    "Order Date : " + order.getOrderDate() + "\n" +
                    "Secret Code: " + order.getSecretCode() + "\n\n" +
                    "Keep your secret code safe; it may be required for verification."
            );
            javaMailSender.send(message);
        } catch (Exception e) {
            log.warn(LOG_PREFIX, "Failed to send order confirmation email", e.getMessage());
        }
    }

    private String generateSecretCode() {
        int code = 100000 + new Random().nextInt(900000);
        return String.valueOf(code);
    }

    private User resolveUser(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}

