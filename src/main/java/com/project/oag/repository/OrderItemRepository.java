package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
	
}