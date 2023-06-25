package com.project.oag.shopping.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/order")
@CrossOrigin("http://localhost:8080/")
public class OrderController {
	 	@Autowired
	    private OrderService orderService;
		public OrderController(OrderService orderService) {
			this.orderService = orderService;
		}
		@PostMapping("/create")
		public ResponseEntity<String> createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long cartId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String phone, @RequestParam String address) {
			String email = userDetails.getUsername();
			Order order = orderService.createOrder(email, cartId, firstName, lastName, phone, address);
			return ResponseEntity.ok("Order created successfully.");
		}

}



