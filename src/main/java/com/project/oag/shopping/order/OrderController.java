package com.project.oag.shopping.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@CrossOrigin("http://localhost:8080/")
public class OrderController {
	 	@Autowired
	    private OrderService orderService;
		public OrderController(OrderService orderService) {
			this.orderService = orderService;
		}


	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		Order savedOrder = orderService.createOrder(order);
		return ResponseEntity.ok(savedOrder);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		Order order = orderService.getOrderById(id);
		if (order != null) {
			return ResponseEntity.ok(order);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		List<Order> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}

}



