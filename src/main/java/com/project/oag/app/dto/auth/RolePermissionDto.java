package com.project.oag.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class RolePermissionDto implements Serializable {
    private long permissionId;
    private String permissionName;
    private boolean assignable;
    @JsonIgnore
    private boolean admin;
}