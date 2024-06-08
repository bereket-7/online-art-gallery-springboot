package com.project.oag.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;
@ConfigurationProperties(prefix = "security-skip-list")
public record SecuritySkipList(
        Map<String, Set<String>> skip) {
}    