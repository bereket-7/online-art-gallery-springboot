package com.project.oag.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "PERMISSION")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERMISSION_ID")
    private Long permissionId;

    @Column(name = "PERMISSION_NAME", nullable = false, unique = true, length = 100)
    private String permissionName;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<UserRole> roles = new HashSet<>();
}
