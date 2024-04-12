package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstname;

    @Column(name = "LAST_NAME")
    private String lastname;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @CreationTimestamp
    @Column(name = "ORDER_DATE")
    private Timestamp orderDate;

    @Column(name = "SECRET_CODE")
    private String secretCode;

    @JsonIgnoreProperties({"id"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID")
    private OrderAddress address;
}
