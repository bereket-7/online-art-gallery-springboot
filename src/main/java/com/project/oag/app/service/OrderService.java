package com.project.oag.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.project.oag.app.model.Order;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.CartRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final JavaMailSender javaMailSender;
	public OrderService(CartRepository cartRepository, OrderRepository orderRepository, JavaMailSender javaMailSender) {
		this.cartRepository = cartRepository;
		this.orderRepository = orderRepository;
		this.javaMailSender = javaMailSender;
	}
	public Order createOrder(Order order) {
		order.setOrderDate(LocalDateTime.now());
		Order savedOrder = orderRepository.save(order);
		sendOrderConfirmationEmail(savedOrder);
		return savedOrder;
	}
	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElse(null);
	}
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	private void sendOrderConfirmationEmail(Order order) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(order.getEmail());
		message.setSubject("Order Confirmation");
		message.setText("Thank you for your order!\n\nOrder Details:\n\n" +
				"First Name: " + order.getFirstname() + "\n" +
				"Last Name: " + order.getLastname() + "\n" +
				"Email: " + order.getEmail() + "\n" +
				"Phone: " + order.getPhone() + "\n" +
				"Address: " + order.getAddress() + "\n" +
				"Order Date: " + order.getOrderDate() + "\n" +
				"Secret Code: " + order.getSecretCode());
		javaMailSender.send(message);
	}
	private String generateSecretCode() {
		Random random = new Random();
		int code = 100000 + random.nextInt(900000);
		return String.valueOf(code);
	}
}
