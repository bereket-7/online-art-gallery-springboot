package com.project.oag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.OrderItem;
import com.project.oag.repository.OrderItemRepository;

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
