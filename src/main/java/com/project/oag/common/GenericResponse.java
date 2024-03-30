package com.project.oag.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

    @Builder
    public record GenericResponse<T>(@JsonProperty("status") int status,
                                     @JsonProperty("message") String message,
                                     @JsonProperty("content") T content) implements Serializable {
    }
