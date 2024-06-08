package com.project.oag.config.security;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
public record AccountLockoutPolicyConfig(
        @NestedConfigurationProperty
        HashMap<String, LockoutRule> lockoutPolicy
) {
}