package com.project.oag.app.controller;

import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.service.OrderService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Create an order from the authenticated buyer's cart */
    @PostMapping
    @PreAuthorize("hasAuthority('USER_ADD_ORDER')")
    public ResponseEntity<GenericResponse> createOrder(HttpServletRequest request,
                                                       @Valid @RequestBody OrderRequestDto orderRequestDto) {
        return prepareResponse(HttpStatus.CREATED, "Order created successfully",
                orderService.createOrder(request, orderRequestDto));
    }

    /** Buyer: view own order history */
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER_VIEW_ORDERS')")
    public ResponseEntity<GenericResponse> getMyOrders(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Orders retrieved", orderService.getMyOrders(request));
    }

    /** Admin: list all orders */
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ORDERS')")
    public ResponseEntity<GenericResponse> getAllOrders() {
        return prepareResponse(HttpStatus.OK, "Orders retrieved", orderService.getAllOrders());
    }

    /** Admin: update order status */
    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ORDER')")
    public ResponseEntity<GenericResponse> updateOrderStatus(@PathVariable Long id,
                                                              @RequestParam OrderStatus status) {
        return prepareResponse(HttpStatus.OK, "Order status updated", orderService.updateOrderStatus(id, status));
    }

    /** Admin: delete an order */
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_ORDER')")
    public ResponseEntity<GenericResponse> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return prepareResponse(HttpStatus.OK, "Order deleted", null);
    }
}

