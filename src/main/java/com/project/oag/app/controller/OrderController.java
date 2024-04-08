package com.project.oag.app.controller;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.service.OrderService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
	private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
	@PreAuthorize("hasAuthority('USER_ADD_ORDER')")
	public ResponseEntity<GenericResponse> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
		return orderService.createOrder(orderRequestDto);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ADMIN_FETCH_ORDERS')")
	public ResponseEntity<GenericResponse> getAllOrders() {
		return orderService.getAllOrders();
	}

	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasAuthority('ADMIN_DELETE_ORDER')")
    public ResponseEntity<GenericResponse> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrdersById(id);
    }

}



