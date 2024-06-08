package com.project.oag.config.security.captcha;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ReCaptchaAttemptService {
    private final LoadingCache<String, Integer> attemptsCache;

    public ReCaptchaAttemptService() {
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public void reCaptchaSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    public void reCaptchaFailed(final String key) {
        int attempts = attemptsCache.getUnchecked(key);
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(final String key, int allowedLimit) {
        return attemptsCache.getUnchecked(key) >= allowedLimit;
    }
}