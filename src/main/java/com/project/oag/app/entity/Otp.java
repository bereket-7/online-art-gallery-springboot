package com.project.oag.app.entity;


import com.project.oag.app.dto.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Setter
@Getter
@Table(name = "ONE_TIME_PASSWORD", indexes = {
        @Index(name = "otp_user_id_index", columnList = "USER_ID"),
        @Index(name = "otp_otp_code_index", columnList = "OTP_CODE"),
        @Index(name = "otp_notification_type_index", columnList = "NOTIFICATION_TYPE"),

})
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OTP_ID", length = 20)
    private long otpId;

    @Column(name = "USER_ID", nullable = false)
    private long userId;

    @Column(name = "OTP_CODE", nullable = false)
    private String otpCode;

    @Column(name = "NOTIFICATION_TYPE", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private NotificationType notificationType;

    @Column(name = "OTP_CREATION_TIME", nullable = false)
    private Timestamp otpCreationTime;

    @Column(name = "OTP_EXPIRY_TIME", nullable = false)
    private Timestamp otpExpiryTime;
}

