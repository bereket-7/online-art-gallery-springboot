package com.project.oag.shopping.order;

import java.util.Date;
import java.util.Optional;

import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.shopping.cart.Cart;
import com.project.oag.shopping.cart.CartRepository;
import com.project.oag.shopping.cart.CartService;
import com.project.oag.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.user.User;
import com.project.oag.exceptions.OrderNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;

	public OrderService(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.orderRepository = orderRepository;
	}

	public Order createOrder(String email, Long cartId, String firstName, String lastName, String phone, String address) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		Optional<Cart> optionalCart = cartRepository.findById(cartId);

		if (optionalUser.isPresent() && optionalCart.isPresent()) {
			User user = optionalUser.get();
			Cart cart = optionalCart.get();
			// Create the order
			Order order = new Order();
			order.setUser(user);
			order.setCart(cart);
			order.setOrderDate(new Date());
			order.setFirstname(firstName);
			order.setLastname(lastName);
			order.setEmail(email);
			order.setPhone(phone);
			order.setAddress(address);
			userRepository.save(user);
			return orderRepository.save(order);
		} else {
			throw new UserNotFoundException("Cart not found.");
		}
	}

	    public Order getOrder(Long orderId) throws OrderNotFoundException {
	        Optional<Order> order = orderRepository.findById(orderId);
	        if (order.isPresent()) {
	            return order.get();
	        }
	        throw new OrderNotFoundException("Order not found");
	    }
}
