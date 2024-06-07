package com.project.oag.app.repository;

import com.project.oag.app.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
    PaymentLog findByToken(String token);

}
