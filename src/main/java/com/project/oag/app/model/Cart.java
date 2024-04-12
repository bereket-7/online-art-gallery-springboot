package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="CART")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "QUANTITY")
    private int quantity;

    @JsonIgnoreProperties({"ratings"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTWORK_ID")
    private Artwork artwork;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Long userId;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

}