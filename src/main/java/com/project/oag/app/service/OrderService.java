package com.project.oag.app.service;

import com.project.oag.app.dto.OrderMapper;
import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderResponseDto;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.entity.Cart;
import com.project.oag.app.entity.CertificateOfAuthenticity;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.OrderAddress;
import com.project.oag.app.entity.OrderItem;
import com.project.oag.app.entity.Shipment;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.CartRepository;
import com.project.oag.app.repository.CertificateOfAuthenticityRepository;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.ShipmentRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.BadRequestException;
import com.project.oag.exceptions.ConflictException;
import com.project.oag.exceptions.ResourceNotFoundException;
import com.project.oag.exceptions.UserAuthorizationException;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.RequestUtils.getLoggedInUserName;

@Service
@Slf4j
public class OrderService {

    private static final int ABANDONED_TTL_MINUTES = 60;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final JavaMailSender javaMailSender;
    private final NotificationWebSocketService notificationService;
    private final CoaService coaService;
    private final ShipmentRepository shipmentRepository;
    private final CertificateOfAuthenticityRepository certificateRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartRepository cartRepository,
                        CartService cartService,
                        JavaMailSender javaMailSender,
                        NotificationWebSocketService notificationService,
                        CoaService coaService,
                        ShipmentRepository shipmentRepository,
                        CertificateOfAuthenticityRepository certificateRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.javaMailSender = javaMailSender;
        this.notificationService = notificationService;
        this.coaService = coaService;
        this.shipmentRepository = shipmentRepository;
        this.certificateRepository = certificateRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(HttpServletRequest request, OrderRequestDto dto) {
        User user = resolveUser(request);

        BigDecimal total = cartRepository.calculateTotalPriceByUserId(user.getId());
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException("Cannot place an order with an empty cart");
        }

        Order order = new Order();
        order.setFirstname(dto.getFirstname());
        order.setLastname(dto.getLastname());
        order.setEmail(dto.getEmail());
        order.setPhone(dto.getPhone());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(total);
        order.setSecretCode(generateSecretCode());
        attachAddress(order, dto.getAddress());

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        for (Cart cartItem : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setArtwork(cartItem.getArtwork());
            item.setArtist(cartItem.getArtwork().getUser());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getArtwork().getPrice());
            item.setLineTotal(cartItem.getArtwork().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            order.getItems().add(item);
        }

        Order saved = orderRepository.save(order);
        log.info(LOG_PREFIX, "Order created", "orderId=" + saved.getId() + " userId=" + user.getId());
        sendOrderConfirmationEmail(saved);
        notificationService.sendUserNotification(user.getEmail(),
                "Order #" + saved.getId() + " initialized pending payment!", "ORDER");
        return toDto(saved);
    }

    @Transactional
    public Order createPendingOrder(User buyer, ArtworkLine line, String firstName, String lastName) {
        Order order = new Order();
        order.setUser(buyer);
        order.setFirstname(firstName != null ? firstName : buyer.getFirstName());
        order.setLastname(lastName != null ? lastName : buyer.getLastName());
        order.setEmail(buyer.getEmail());
        order.setPhone(buyer.getPhone());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(line.lineTotal());
        order.setSecretCode(generateSecretCode());

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setArtwork(line.artwork());
        item.setArtist(line.artist());
        item.setQuantity(line.quantity());
        item.setUnitPrice(line.unitPrice());
        item.setLineTotal(line.lineTotal());
        order.getItems().add(item);
        return orderRepository.save(order);
    }

    @Transactional
    public void fulfillOrderAfterPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.isFulfilled()) {
            log.info(LOG_PREFIX, "Order already fulfilled, skipping", "orderId=" + orderId);
            return;
        }
        try {
            order.getItems().forEach(item ->
                    cartService.decrementQuantityForArtwork(item.getArtwork().getId(), item.getQuantity())
            );
        } catch (Exception e) {
            throw new ConflictException("Stock conflict while fulfilling order " + orderId);
        }
        List<Long> artworkIds = order.getItems().stream()
                .map(item -> item.getArtwork().getId())
                .toList();
        if (!artworkIds.isEmpty()) {
            cartRepository.deleteByUserIdAndArtworkIdIn(order.getUser().getId(), artworkIds);
        }
        coaService.issueForOrder(order);
        order.setFulfilled(true);
        orderRepository.save(order);
        log.info(LOG_PREFIX, "Order fulfilled after payment", "orderId=" + orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders(HttpServletRequest request) {
        User user = resolveUser(request);
        return orderRepository.findByUserId(user.getId()).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(HttpServletRequest request, Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        User user = resolveUser(request);
        boolean admin = hasAdminFetch(user);
        if (!admin && !order.getUser().getId().equals(user.getId())) {
            throw new UserAuthorizationException("Not allowed to view this order");
        }
        return toDto(order);
    }

    public List<CertificateOfAuthenticity> getCertificatesForOrder(HttpServletRequest request, Long orderId) {
        getOrderById(request, orderId);
        return certificateRepository.findByOrderItemOrderId(orderId);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long id, OrderStatus status) {
        val order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        notificationService.sendUserNotification(saved.getEmail(),
                "Your order #" + saved.getId() + " is now " + status, "ORDER");
        return toDto(saved);
    }

    @Transactional
    public void deleteOrderById(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderRepository.deleteById(id);
    }

    @Transactional
    public int cancelAbandonedPendingOrders() {
        Timestamp cutoff = Timestamp.from(Instant.now().minus(ABANDONED_TTL_MINUTES, ChronoUnit.MINUTES));
        List<Order> abandoned = orderRepository.findByStatusAndFulfilledFalseAndOrderDateBefore(OrderStatus.PENDING, cutoff);
        abandoned.forEach(order -> order.setStatus(OrderStatus.CANCELLED));
        orderRepository.saveAll(abandoned);
        if (!abandoned.isEmpty()) {
            log.info(LOG_PREFIX, "Cancelled abandoned pending orders", "count=" + abandoned.size());
        }
        return abandoned.size();
    }

    private OrderResponseDto toDto(Order order) {
        Shipment shipment = shipmentRepository.findByOrderId(order.getId()).orElse(null);
        List<CertificateOfAuthenticity> certificates = certificateRepository.findByOrderItemOrderId(order.getId());
        return OrderMapper.toDto(order, shipment, certificates);
    }

    private void attachAddress(Order order, OrderAddress address) {
        if (address == null) {
            return;
        }
        address.setId(null);
        address.setOrder(order);
        order.setAddress(address);
    }

    private boolean hasAdminFetch(User user) {
        if (user.getUserRole() == null || user.getUserRole().getPermissions() == null) {
            return false;
        }
        return user.getUserRole().getPermissions().stream()
                .anyMatch(p -> "ADMIN_FETCH_ORDERS".equals(p.getPermissionName()));
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

    public record ArtworkLine(com.project.oag.app.entity.Artwork artwork,
                              User artist,
                              int quantity,
                              BigDecimal unitPrice,
                              BigDecimal lineTotal) {
    }
}
