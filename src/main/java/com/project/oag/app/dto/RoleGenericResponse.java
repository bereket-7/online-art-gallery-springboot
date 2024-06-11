package com.project.oag.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.oag.app.dto.auth.UserRoleDto;

import java.io.Serializable;
import java.util.List;

public record RoleGenericResponse(@JsonProperty("status") int status, @JsonProperty("message") String message,
                                  @JsonProperty("content") List<UserRoleDto> content) implements Serializable {
}
