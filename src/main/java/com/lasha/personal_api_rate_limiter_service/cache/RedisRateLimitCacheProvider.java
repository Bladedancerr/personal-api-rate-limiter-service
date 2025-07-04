package com.lasha.personal_api_rate_limiter_service.cache;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class RedisRateLimitCacheProvider {
}
