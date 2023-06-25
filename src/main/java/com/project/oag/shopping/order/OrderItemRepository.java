package com.project.oag.shopping.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.shopping.order.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
	
}