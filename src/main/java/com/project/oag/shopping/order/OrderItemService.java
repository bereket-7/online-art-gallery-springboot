package com.project.oag.shopping.order;

import com.project.oag.shopping.order.OrderItem;
import com.project.oag.shopping.order.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    public void addOrderedProducts(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

}
