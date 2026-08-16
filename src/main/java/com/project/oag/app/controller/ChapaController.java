package com.project.oag.app.controller;

import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.dto.PaymentStatus;
import com.project.oag.app.entity.Order;
import com.project.oag.app.entity.PaymentLog;
import com.project.oag.app.repository.OrderRepository;
import com.project.oag.app.service.ChapaService;
import com.project.oag.app.service.OrderService;
import com.project.oag.app.service.PayoutService;
import com.project.oag.common.GenericResponse;
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
    private final OrderService orderService;
    private final PayoutService payoutService;

    public ChapaController(ChapaService chapaService, OrderRepository orderRepository,
                           OrderService orderService, PayoutService payoutService) {
        this.chapaService = chapaService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.payoutService = payoutService;
    }

    @GetMapping("/callback")
    public ResponseEntity<GenericResponse> verifyCallback(@RequestParam("tx_ref") String txRef) throws Throwable {
        PaymentLog log = chapaService.verify(txRef);

        Optional<Order> orderOpt = orderRepository.findByPaymentLog_Token(txRef);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.isFulfilled()) {
                return prepareResponse(HttpStatus.OK, "Payment already processed", log.getPaymentStatus());
            }
            if (PaymentStatus.VERIFIED.equals(log.getPaymentStatus())) {
                order.setStatus(OrderStatus.CONFIRMED);
                payoutService.creditOnPayment(order);
                orderService.fulfillOrderAfterPayment(order.getId());
            } else {
                order.setStatus(OrderStatus.CANCELLED);
            }
            orderRepository.save(order);
        }

        return prepareResponse(HttpStatus.OK, "Payment Verification Processed", log.getPaymentStatus());
    }
}
