package com.lasha.personal_api_rate_limiter_service.cache.old;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lasha.personal_api_rate_limiter_service.pojo.RateLimitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
//implements RateLimitCacheProvider
public class RedisRateLimitCacheProvider  {

//    private final StringRedisTemplate redisTemplate;
////    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public RedisRateLimitCacheProvider(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
//        this.redisTemplate = redisTemplate;
////        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public RateLimitInfo get(String clientId, String userId) {
//        String key = key(clientId, userId);
//        String cacheValue = redisTemplate.opsForValue().get(key);
//        if(cacheValue == null) {
//            return null;
//        }
//
//        RateLimitInfo rateLimitInfo = null;
//
//        try {
//            rateLimitInfo = objectMapper.readValue(cacheValue, RateLimitInfo.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return rateLimitInfo;
//    }
//
//    @Override
//    public void save(String clientId, String userId, RateLimitInfo rateLimitInfo) {
//        String key = key(clientId, userId);
//        if (rateLimitInfo == null) {
//            throw new IllegalArgumentException("RateLimitInfo cannot be null");
//        }
//
//        try {
//            String valueToCache = objectMapper.writeValueAsString(rateLimitInfo);
//            redisTemplate.opsForValue().set(key, valueToCache, rateLimitInfo.getWindowEndMillis() - System.currentTimeMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Error serializing RateLimitInfo to JSON", e);
//        }
//    }
//
//    @Override
//    public void clear(String clientId, String userId) {
//        redisTemplate.delete(key(clientId, userId));
//    }
}
