package com.lasha.personal_api_rate_limiter_service.cache;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory implementation of RateLimitStore for development profile.
 * Stores counters, expiries, and active user sets in memory (not persistent).
 */
@Component
@Profile("dev")
public class InMemoryRateLimitStore implements RateLimitStore {
    // Request counts per (clientId:userId)
    private final ConcurrentHashMap<String, AtomicInteger> requestRates = new ConcurrentHashMap<>();

    // Expiry times per (clientId:userId)
    private final ConcurrentHashMap<String, Long> ttls = new ConcurrentHashMap<>();

    // Set of active users per client
    private final ConcurrentHashMap<String, Set<String>> activeUsers = new ConcurrentHashMap<>();

    /**
     * Increments and returns the request count for this user in this window.
     */
    @Override
    public int incrementRequestRate(String clientId, String userId) {
        String key = rateCountKey(clientId, userId);
        AtomicInteger count = requestRates.computeIfAbsent(key, k -> new AtomicInteger(0));
        return count.incrementAndGet();
    }

    /**
     * Gets the request count for a user in the current window.
     */
    @Override
    public int getRequestRate(String clientId, String userId) {
        String key = rateCountKey(clientId, userId);
        AtomicInteger count = requestRates.get(key);
        if (count == null) {
            return 0;
        }
        return count.get();
    }

    /**
     * Resets the request rate and TTL for the user, and removes them from active users.
     */
    @Override
    public void resetRequestRate(String clientId, String userId) {
        String key = rateCountKey(clientId, userId);

        requestRates.remove(key);
        ttls.remove(key);

        // Remove user from active users
        Set<String> users = activeUsers.get(clientId);
        if (users != null) {
            synchronized (users) {
                users.remove(userId);
            }
        }
    }

    /**
     * Sets the expiration time (window duration) for the user's request window.
     * The value is an absolute timestamp (System.currentTimeMillis() + windowMillis).
     */
    @Override
    public void setTtl(String clientId, String userId, long windowMillis) {
        String key = rateCountKey(clientId, userId);
        long expiresAt = System.currentTimeMillis() + windowMillis;
        ttls.put(key, expiresAt);
    }

    /**
     * Gets the milliseconds left until this user's rate window expires.
     * Returns 0 if expired or not found.
     */
    @Override
    public long getTtl(String clientId, String userId) {
        String key = rateCountKey(clientId, userId);
        Long expiresAt = ttls.get(key);
        if (expiresAt == null) {
            return 0L;
        }
        long remaining = expiresAt - System.currentTimeMillis();
        return Math.max(0L, remaining);
    }

    /**
     * Adds the user to the active user set for the client.
     * Called on every request to ensure user is tracked.
     */
    @Override
    public void addOrUpdateUserActivity(String clientId, String userId) {
        Set<String> users = activeUsers.computeIfAbsent(clientId, k -> ConcurrentHashMap.newKeySet());
        users.add(userId);
    }

    /**
     * Returns the number of currently active users for the client.
     * (Some may be expired until cleanup is called.)
     */
    @Override
    public int getUniqueUserCount(String clientId) {
        Set<String> users = activeUsers.get(clientId);
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    /**
     * Removes users from the active user set whose rate window has expired.
     * Also cleans up related counters and TTLs.
     * Should be run before counting users or periodically.
     */
    @Override
    public void cleanupExpiredUsers(String clientId) {
        Set<String> users = activeUsers.get(clientId);
        if (users == null) {
            return;
        }
        // Synchronize to avoid concurrent modification
        synchronized (users) {
            users.removeIf(userId -> {
                long ttl = getTtl(clientId, userId);
                if (ttl <= 0) {
                    String key = rateCountKey(clientId, userId);
                    requestRates.remove(key);
                    ttls.remove(key);
                    return true;
                }
                return false;
            });
        }
    }
}