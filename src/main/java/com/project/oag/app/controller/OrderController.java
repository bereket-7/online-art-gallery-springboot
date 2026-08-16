package com.project.oag.app.controller;

import com.project.oag.app.dto.CommerceMappers;
import com.project.oag.app.dto.OrderRequestDto;
import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.dto.StatusUpdateDto;
import com.project.oag.app.service.OrderService;
import com.project.oag.common.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/orders")
@Tag(name = "Orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Unpaid draft from cart. Paid checkout is POST /checkout. */
    @PostMapping
    @PreAuthorize("hasAuthority('USER_ADD_ORDER')")
    public ResponseEntity<GenericResponse> createOrder(HttpServletRequest request,
                                                       @Valid @RequestBody OrderRequestDto orderRequestDto) {
        return prepareResponse(HttpStatus.CREATED, "Order created successfully",
                orderService.createOrder(request, orderRequestDto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW_ORDERS')")
    public ResponseEntity<GenericResponse> getOrders(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Orders retrieved", orderService.getMyOrders(request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER_VIEW_ORDERS')")
    public ResponseEntity<GenericResponse> getMyOrders(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Orders retrieved", orderService.getMyOrders(request));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_ORDERS')")
    public ResponseEntity<GenericResponse> getAllOrders() {
        return prepareResponse(HttpStatus.OK, "Orders retrieved", orderService.getAllOrders());
    }

    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ORDER')")
    public ResponseEntity<GenericResponse> updateOrderStatus(@PathVariable Long id,
                                                              @RequestParam(required = false) OrderStatus status,
                                                              @RequestBody(required = false) StatusUpdateDto body) {
        OrderStatus next = status != null ? status : (body != null ? body.getStatus() : null);
        return prepareResponse(HttpStatus.OK, "Order status updated", orderService.updateOrderStatus(id, next));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_ORDER')")
    public ResponseEntity<GenericResponse> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return prepareResponse(HttpStatus.OK, "Order deleted", null);
    }

    @GetMapping("/{id}/certificates")
    @PreAuthorize("hasAnyAuthority('USER_VIEW_ORDERS', 'ADMIN_FETCH_ORDERS')")
    public ResponseEntity<GenericResponse> getCertificates(HttpServletRequest request, @PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Certificates retrieved",
                orderService.getCertificatesForOrder(request, id).stream()
                        .map(CommerceMappers::toCertificateDto)
                        .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_VIEW_ORDERS', 'ADMIN_FETCH_ORDERS')")
    public ResponseEntity<GenericResponse> getOrder(HttpServletRequest request, @PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Order retrieved", orderService.getOrderById(request, id));
    }
}
