package com.project.oag.app.repository;

import com.project.oag.app.dto.OrderStatus;
import com.project.oag.app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.user.id = :userId order by o.orderDate desc")
    List<Order> findByUserId(Long userId);

    Optional<Order> findByPaymentLog_Token(String token);

    List<Order> findByStatusAndFulfilledFalseAndOrderDateBefore(OrderStatus status, Timestamp cutoff);

    @Query("""
            select (count(oi) > 0) from OrderItem oi
            where oi.order.user.id = :userId
              and oi.artwork.id = :artworkId
              and oi.order.status in :statuses
            """)
    boolean existsPurchasedArtwork(@Param("userId") Long userId,
                                   @Param("artworkId") Long artworkId,
                                   @Param("statuses") List<OrderStatus> statuses);
}

