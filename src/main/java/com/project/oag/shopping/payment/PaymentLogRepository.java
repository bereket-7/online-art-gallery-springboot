package com.project.oag.shopping.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLogRepository  extends JpaRepository<PaymentLog, Long> {
    PaymentLog findByToken(String token);

}
