package com.lasha.personal_api_rate_limiter_service.cache.old;

import com.lasha.personal_api_rate_limiter_service.pojo.RateLimitInfo;

public interface RateLimitCacheProvider {

    // Interface for Rate Limit Cache Provider
    public RateLimitInfo get(String clientId, String userId);
    public void save(String clientId, String userId, RateLimitInfo rateLimitInfo);
    public void clear(String clientId, String userId);

    public default String key(String clientId, String userId) {
        return clientId + ":" + userId;
    }
}
