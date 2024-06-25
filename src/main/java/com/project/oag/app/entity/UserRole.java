package com.project.oag.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Entity
@Cacheable
@Table(name = "USER_ROLE", indexes = {
        @Index(name = "u_r_role_name_index", columnList = "ROLE_NAME"),
        @Index(name = "u_r_issuer_user_id_index", columnList = "ISSUER_USER_ID")}
)
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "ROLE_NAME", nullable = false)
    private String roleName;

    @Column(name = "IS_ADMIN", nullable = false)
    private boolean admin;

    @Column(name = "ISSUER_USER_ID")
    private Long issuerUserId;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;

}
