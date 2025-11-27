package com.client.notification.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;
    
    // Allow 10 emails per minute for demo purposes
    private static final int MAX_REQUESTS = 10;
    private static final int WINDOW_SECONDS = 60;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {
        String key = "rate_limit:" + userId;
        
        // Atomic increment
        Long count = redisTemplate.opsForValue().increment(key);
        
        // If this is the first request, set the expiry window
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }

        return count != null && count <= MAX_REQUESTS;
    }
}

