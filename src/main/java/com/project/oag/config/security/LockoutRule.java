package com.project.oag.config.security;

public record LockoutRule(
        short failureCount,
        long blockTime) {
}
