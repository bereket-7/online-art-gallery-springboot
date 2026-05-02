package com.project.oag.app.controller;

import com.project.oag.app.service.ChapaService;
import com.project.oag.app.entity.PaymentLog;
import com.project.oag.app.entity.Order;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.app.dto.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/chapa")
public class ChapaController {
    
    private final ChapaService chapaService;
    private final OrderRepository orderRepository;

    public ChapaController(ChapaService chapaService, OrderRepository orderRepository) {
        this.chapaService = chapaService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/callback")
    public ResponseEntity<GenericResponse> verifyCallback(@RequestParam("tx_ref") String txRef) throws Throwable {
        PaymentLog log = chapaService.verify(txRef);
        
        Optional<Order> orderOpt = orderRepository.findByPaymentLog_Token(txRef);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if ("VERIFIED".equals(log.getPaymentStatus().name())) {
                order.setStatus(OrderStatus.CONFIRMED);
            } else {
                order.setStatus(OrderStatus.CANCELLED);
            }
            orderRepository.save(order);
        }
        
        return prepareResponse(HttpStatus.OK, "Payment Verification Processed", log.getPaymentStatus());
    }
}
