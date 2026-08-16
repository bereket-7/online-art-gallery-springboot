package com.project.oag.app.entity;

import com.project.oag.app.dto.PayoutStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payout_request")
public class PayoutRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private User artist;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayoutStatus status = PayoutStatus.PENDING;

    @CreationTimestamp
    @Column(name = "requested_at")
    private Timestamp requestedAt;

    @Column(name = "processed_at")
    private Timestamp processedAt;

    @Column(name = "external_ref")
    private String externalRef;

    @Column(name = "manual", nullable = false)
    private boolean manual = true;
}
