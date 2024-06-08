package com.project.oag.app.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class UserRoleDto implements Serializable {
    @JsonProperty("roleId")
    private Long roleId;

    @NotEmpty
    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("isAdmin")
    @JsonIgnore
    private boolean admin;


    @JsonProperty("issuerUserId")
    @JsonIgnore
    private long issuerUserId;

    @JsonIgnore
    @Transient
    private List<UserDto> user;

    @JsonProperty("rolePermission")
    private Set<RolePermissionDto> rolePermissions;

}
