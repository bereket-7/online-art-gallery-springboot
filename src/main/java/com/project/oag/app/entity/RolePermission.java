package com.project.oag.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ROLE_PERMISSION", indexes = {
        @Index(name = "per_name_index", columnList = "PERMISSION_NAME"),
        @Index(name = "per_assignable_index", columnList = "IS_ASSIGNABLE"),
        @Index(name = "per_admin_index", columnList = "IS_ADMIN")
})
@Cacheable
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERMISSION_ID", nullable = false)
    private long permissionId;

    @Column(name = "PERMISSION_NAME", unique = true, nullable = false)
    private String permissionName;

    @Column(name = "IS_ASSIGNABLE", nullable = false)
    private boolean assignable;

    @Column(name = "IS_ADMIN", nullable = false)
    private boolean admin;

}
