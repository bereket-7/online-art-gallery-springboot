package com.project.oag.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "security-init-config")
public record RolePermissionConfig(boolean initial,
                                   boolean reset,
                                   Set<String> roles,
                                   Set<String> userFlag,
                                   @NestedConfigurationProperty
                                   Map<String, ConfiguredUser> users,
                                   @NestedConfigurationProperty
                                   HashMap<String, LockoutRule> lockoutPolicy) {
}
