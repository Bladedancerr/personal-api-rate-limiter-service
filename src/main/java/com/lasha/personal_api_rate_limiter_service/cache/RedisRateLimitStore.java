package com.lasha.personal_api_rate_limiter_service.cache;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis implementation of RateLimitStore for production profile.
 * Stores counters and active users in Redis, leveraging atomic operations and expiries.
 */
@Component
@Profile("prod")
public class RedisRateLimitStore implements RateLimitStore {
    private final StringRedisTemplate redisTemplate;
    private static final long ACTIVE_SET_TTL_BUFFER = 60000L; // 60 seconds buffer for active set

    public RedisRateLimitStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Increments the request count for (client, user).
     * The key is incremented atomically by Redis, so no explicit locking is needed.
     * If this is the first request for the window, caller must also set TTL!
     */
    @Override
    public int incrementRequestRate(String clientId, String userId) {
        Long val = redisTemplate.opsForValue().increment(rateCountKey(clientId, userId), 1);
        if (val == null) {
            return 0;
        }
        return val.intValue();
    }

    /**
     * Returns the current request count for (client, user).
     * Returns 0 if the key does not exist.
     */
    @Override
    public int getRequestRate(String clientId, String userId) {
        String value = redisTemplate.opsForValue().get(rateCountKey(clientId, userId));
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Removes the request rate counter and removes user from active set.
     * This is typically called when the window expires or on reset.
     */
    @Override
    public void resetRequestRate(String clientId, String userId) {
        redisTemplate.delete(rateCountKey(clientId, userId));
        redisTemplate.opsForSet().remove(activeSetKey(clientId), userId);
    }

    /**
     * Sets the TTL (rate limit window) on the (client, user) key.
     * Must be called after incrementing, to ensure the key expires after the window.
     */
    @Override
    public void setTtl(String clientId, String userId, long windowMillis) {
        if (windowMillis > 0) {
            // Set TTL on the rate counter key
            redisTemplate.expire(rateCountKey(clientId, userId), windowMillis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Returns the remaining TTL in milliseconds for the (client, user) key.
     * Returns 0 if expired or the key doesn't exist.
     */
    @Override
    public long getTtl(String clientId, String userId) {
        Long ttl = redisTemplate.getExpire(rateCountKey(clientId, userId), TimeUnit.MILLISECONDS);
        if (ttl == null || ttl < 0) {
            return 0L;
        }
        return ttl;
    }

    /**
     * Adds or updates a user's activity by including them in the active set for the client.
     * Used to track unique users per client during the window.
     */
    @Override
    public void addOrUpdateUserActivity(String clientId, String userId) {
        redisTemplate.opsForSet().add(activeSetKey(clientId), userId);
        Set<String> members = redisTemplate.opsForSet().members(activeSetKey(clientId));
    }

    /**
     * Returns the count of unique active users for a client.
     * Uses Redis's set cardinality operation for performance.
     */
    @Override
    public int getUniqueUserCount(String clientId) {
        Long size = redisTemplate.opsForSet().size(activeSetKey(clientId));
        Set<String> members = redisTemplate.opsForSet().members(activeSetKey(clientId));
        if (size == null) {
            return 0;
        }
        return size.intValue();
    }

    /**
     * Cleans up users whose window expired, by checking if their counter key still exists.
     * Removes users from the active set if they no longer have a valid rate counter key.
     * Should be run periodically or before counting unique users.
     */
    @Override
    public void cleanupExpiredUsers(String clientId) {
        Set<String> users = redisTemplate.opsForSet().members(activeSetKey(clientId));
        if (users == null || users.isEmpty()) {
            return;
        }

        for (String userId : users) {
            String key = rateCountKey(clientId, userId);
            Boolean exists = redisTemplate.hasKey(key);
            if (exists == null || !exists) {
                redisTemplate.opsForSet().remove(activeSetKey(clientId), userId);
            }
        }

        Set<String> remainingUsers = redisTemplate.opsForSet().members(activeSetKey(clientId));
    }

    // Helper to create the Redis Set key for all active users for a client
    private String activeSetKey(String clientId) {
        return "active:" + clientId;
    }
}