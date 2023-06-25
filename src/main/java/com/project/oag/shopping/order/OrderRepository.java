package com.project.oag.shopping.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.shopping.order.Order;
import com.project.oag.user.User;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long> {
}
