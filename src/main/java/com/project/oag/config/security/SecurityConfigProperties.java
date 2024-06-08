package com.project.oag.config.security;

import java.util.Set;

public record SecurityConfigProperties(
        Set<String> securitySkipList
) {
}
