package com.project.oag.app.repository;

import com.project.oag.app.entity.CertificateOfAuthenticity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateOfAuthenticityRepository extends JpaRepository<CertificateOfAuthenticity, Long> {
    Optional<CertificateOfAuthenticity> findByVerificationCode(String verificationCode);

    Optional<CertificateOfAuthenticity> findByOrderItemId(Long orderItemId);

    List<CertificateOfAuthenticity> findByOrderItemOrderId(Long orderId);
}
