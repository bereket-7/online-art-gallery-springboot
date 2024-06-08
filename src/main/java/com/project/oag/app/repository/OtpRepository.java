package com.project.oag.app.repository;


import com.project.oag.app.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    List<Otp> findByUserIdAndOtpExpiryTimeAfter(long userId, Timestamp otpExpiryTime);

    List<Otp> findByUserIdAndOtpExpiryTimeAfterOrderByOtpExpiryTimeDesc(long userId, Timestamp otpExpiryTime);

    Optional<Otp> findByOtpCode(String otpCode);
}