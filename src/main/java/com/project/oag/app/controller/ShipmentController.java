package com.project.oag.app.controller;

import com.project.oag.app.entity.Shipment;
import com.project.oag.app.service.ShipmentService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PatchMapping("/{orderId}/ship")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_ORDER')")
    public ResponseEntity<GenericResponse> shipOrder(@PathVariable Long orderId,
                                                     @RequestParam String carrier,
                                                     @RequestParam String trackingNumber) {
        Shipment shipment = shipmentService.shipOrder(orderId, carrier, trackingNumber);
        return prepareResponse(HttpStatus.OK, "Order shipped", shipment);
    }

    @GetMapping("/{orderId}/tracking")
    @PreAuthorize("hasAnyAuthority('USER_VIEW_ORDERS', 'ADMIN_FETCH_ORDERS')")
    public ResponseEntity<GenericResponse> getTracking(@PathVariable Long orderId) {
        return prepareResponse(HttpStatus.OK, "Tracking retrieved", shipmentService.getTracking(orderId));
    }
}
