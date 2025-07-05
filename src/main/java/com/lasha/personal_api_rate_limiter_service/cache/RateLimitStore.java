package com.lasha.personal_api_rate_limiter_service.cache;

/**
 * Base interface for storing and managing rate limits and active user tracking,
 * for both in-memory and Redis implementations.
 */
public interface RateLimitStore {

    /**
     * Atomically increments the request count for a specific (client, user) key.
     * Returns the new request count after increment.
     */
    int incrementRequestRate(String clientId, String userId);

    /**
     * Returns the current request count for the given (client, user).
     * Returns 0 if not found.
     */
    int getRequestRate(String clientId, String userId);

    /**
     * Resets the request rate and TTL for (client, user) and removes them from the active user set.
     * Used when a rate window expires or for manual reset.
     */
    void resetRequestRate(String clientId, String userId);

    /**
     * Sets the expiration time (TTL) in milliseconds from now for this user's rate window.
     * (Should be called after increment, to start/reset the window.)
     */
    void setTtl(String clientId, String userId, long windowMillis);

    /**
     * Gets the remaining TTL (milliseconds) for this user's window.
     * Returns 0 if expired or not found.
     */
    long getTtl(String clientId, String userId);

    /**
     * Registers user activity in the active user set for this client.
     * Used to track unique users in the current window.
     */
    void addOrUpdateUserActivity(String clientId, String userId);

    /**
     * Returns the current number of unique active users for the client.
     */
    int getUniqueUserCount(String clientId);

    /**
     * Cleans up expired users for the given client, removing those whose window is over.
     * Should be called regularly (scheduled or before user counting).
     */
    void cleanupExpiredUsers(String clientId);

    /**
     * Builds a key for the given (client, user) pair.
     * Used for consistent storage.
     */
    default String rateCountKey(String clientId, String userId) {
        return clientId + ":" + userId;
    }
}