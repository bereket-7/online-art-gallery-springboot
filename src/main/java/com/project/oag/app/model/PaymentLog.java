package com.project.oag.app.model;

import com.project.oag.app.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENT_LOG")
public class PaymentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Long userId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private PaymentStatus paymentStatus;
}
