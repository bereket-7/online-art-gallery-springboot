package com.project.oag.config.cache;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.project.oag.common.AppConstants.CACHE_EXPIRY_TIME;

@Component
@Slf4j
public class CacheManagerService {
    private final CacheManager cacheManager;

    public CacheManagerService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void evictSingleCacheValue(String cacheName, String cacheKey) {
        try {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).evict(cacheKey);
            log.info("Cleared cache for cache: {}  and key: {}", cacheName, cacheKey);
        } catch (Exception e) {
            log.warn("Failed to clear cache for cache: {}  and key: {}", cacheName, cacheKey);
        }
    }

    public void evictAllCacheValues(String cacheName) {
        try {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
            log.info("Cleared all cache for cache: {}", cacheName);
        } catch (Exception e) {
            log.warn("Failed to clear cache for cache: {} ", cacheName);
        }
    }

    public void evictAllCaches() {
        try {
            cacheManager.getCacheNames()
                    .parallelStream()
                    .filter(c -> ObjectUtils.isNotEmpty(getCache(c)))
                    .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
            log.info("Cleared all caches.");
        } catch (Exception e) {
            log.warn("Failed to clear all caches.");
        }
    }

    public void putToCache(String cacheName, String key, String value) {
        getCache(cacheName).put(key, value);
    }

    @NotNull
    private Cache getCache(String cacheName) {
        return cacheManager.getCache(cacheName);
    }

    public String getFromCache(String cacheName, String key) {
        String value = null;
        if (getCache(cacheName).get(key) != null) {
            value = getCache(cacheName).get(key).get().toString();
        }
        return value;
    }

    @Scheduled(fixedRate = CACHE_EXPIRY_TIME)
    public void evictAllCachesAtIntervals() {
        evictAllCaches();
    }
}
