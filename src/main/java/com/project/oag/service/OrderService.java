package com.project.oag.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.CartDto;
import com.project.oag.controller.dto.CartItemDto;
import com.project.oag.entity.Order;
import com.project.oag.entity.OrderItem;
import com.project.oag.user.User;
import com.project.oag.exceptions.OrderNotFoundException;
import com.project.oag.repository.OrderItemRepository;
import com.project.oag.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	
	 	@Autowired
	    private CartService cartService;

	    @Autowired
	    OrderRepository orderRepository;

	    @Autowired
	    OrderItemRepository orderItemRepository;

//	    public void placeOrder(User user, String sessionId) {
//	        CartDto cartDto = user.listCartItems();
//	        List<CartItemDto> cartItemDtoList = cartDto.getcartItems();
//
//	        // create the order and save it
//	        Order newOrder = new Order();
//	        newOrder.setCreatedDate(new Date());
//	        newOrder.setSessionId(sessionId);
//	        newOrder.setUser(user);
//	        newOrder.setPrice(cartDto.getTotalCost());
//	        orderRepository.save(newOrder);
//
//	        for (CartItemDto cartItemDto : cartItemDtoList) {
//	            // create orderItem and save each one
//	            OrderItem orderItem = new OrderItem();
//	            orderItem.setCreatedDate(new Date());
//	            orderItem.setPrice(cartItemDto.getArtwork().getPrice());
//	            orderItem.setArtwork(cartItemDto.getArtwork());
//	            orderItem.setQuantity(cartItemDto.getQuantity());
//	            orderItem.setOrder(newOrder);
//	            // add to order item list
//	            orderItemRepository.save(orderItem);
//	        }
//	        cartService.clearCart(user.getUsername());
//	    }

	    public List<Order> listOrders(User user) {
	        return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
	    }


	    public Order getOrder(Integer orderId) throws OrderNotFoundException {
	        Optional<Order> order = orderRepository.findById(orderId);
	        if (order.isPresent()) {
	            return order.get();
	        }
	        throw new OrderNotFoundException("Order not found");
	    }
}
