package com.project.oag.config.security.captcha;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ReCaptchaAttemptService {
    private final StringRedisTemplate redisTemplate;

    public ReCaptchaAttemptService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getRedisKey(String key) {
        return "recaptcha:attempts:" + key;
    }

    public void reCaptchaSucceeded(final String key) {
        redisTemplate.delete(getRedisKey(key));
    }

    public void reCaptchaFailed(final String key) {
        String redisKey = getRedisKey(key);
        Long attempts = redisTemplate.opsForValue().increment(redisKey, 1);
        if (attempts != null && attempts == 1) {
            redisTemplate.expire(redisKey, 4, TimeUnit.HOURS);
        }
    }

    public boolean isBlocked(final String key, int allowedLimit) {
        String attemptsStr = redisTemplate.opsForValue().get(getRedisKey(key));
        if (attemptsStr == null) {
            return false;
        }
        try {
            return Integer.parseInt(attemptsStr) >= allowedLimit;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}