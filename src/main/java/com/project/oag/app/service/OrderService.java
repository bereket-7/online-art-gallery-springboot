package com.project.oag.app.service;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.model.Order;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import jakarta.transaction.Transactional;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static com.project.oag.utils.Utils.prepareResponse;

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
	public ResponseEntity<GenericResponse> createOrder(OrderRequestDto orderRequestDto) {
		try {
			val order = modelMapper.map(orderRequestDto, Order.class);
			order.setSecretCode(generateSecretCode());
			val savedOrder = orderRepository.save(order);
			sendOrderConfirmationEmail(savedOrder);
			return prepareResponse(HttpStatus.OK, "Order created successfully",savedOrder);
		} catch (Exception e) {
			throw new GeneralException("failed to create order");
		}
	}
	public ResponseEntity<GenericResponse> getAllOrders() {
		try {
			List<Order> orders = orderRepository.findAll();
			return prepareResponse(HttpStatus.OK, "Available orders",orders);
		} catch (Exception e) {
			throw new GeneralException("Could not find all orders");
		}
	}
	public ResponseEntity<GenericResponse> deleteOrdersById(final Long id) {
		try {
			orderRepository.deleteById(id);
			return prepareResponse(HttpStatus.OK, "order successfully deleted", null);
		} catch (Exception e) {
			throw new GeneralException("Failed to delete order");
		}
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
