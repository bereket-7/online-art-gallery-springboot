package com.project.oag.app.service;

import com.project.oag.app.dto.ShipmentStatus;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.Shipment;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.repository.ShipmentRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, OrderRepository orderRepository) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Shipment shipOrder(Long orderId, String carrier, String trackingNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Shipment shipment = shipmentRepository.findByOrderId(orderId).orElseGet(() -> {
            Shipment s = new Shipment();
            s.setOrder(order);
            return s;
        });
        shipment.setCarrier(carrier);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setStatus(ShipmentStatus.SHIPPED);
        shipment.setShippedAt(new Timestamp(System.currentTimeMillis()));
        return shipmentRepository.save(shipment);
    }

    public Shipment getTracking(Long orderId) {
        return shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found for order"));
    }
}
