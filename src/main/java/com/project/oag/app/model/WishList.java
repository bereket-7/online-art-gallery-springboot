package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WISHLIST")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @JsonIgnore
    @OneToOne()
    @JoinColumn(name = "USER_ID")
    private Long userId;

    @JsonIgnoreProperties({"artist", "carts", "status"})
    @ManyToOne()
    @JoinColumn(name = "ARTWORK_ID")
    private Long artworkId;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;
}
