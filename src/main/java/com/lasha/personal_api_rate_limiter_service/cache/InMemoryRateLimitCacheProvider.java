package com.lasha.personal_api_rate_limiter_service.cache;

import com.lasha.personal_api_rate_limiter_service.pojo.RateLimitInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("dev")
public class InMemoryRateLimitCacheProvider implements RateLimitCacheProvider
{
    private final ConcurrentHashMap<String, RateLimitInfo> cache = new ConcurrentHashMap<>();

    @Override
    public RateLimitInfo get(String clientId, String userId) {
        return cache.get(key(clientId, userId));
    }

    @Override
    public void save(String clientId, String userId, RateLimitInfo rateLimitInfo) {
        cache.put(key(clientId, userId), rateLimitInfo);
    }

    @Override
    public void clear(String clientId, String userId) {
        cache.remove(key(clientId, userId));
    }
}
