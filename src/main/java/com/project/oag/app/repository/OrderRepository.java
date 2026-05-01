package com.project.oag.app.repository;

import com.project.oag.app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.user.id = :userId order by o.orderDate desc")
    List<Order> findByUserId(Long userId);
}

