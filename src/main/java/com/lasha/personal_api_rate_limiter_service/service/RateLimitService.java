package com.lasha.personal_api_rate_limiter_service.service;

import com.lasha.personal_api_rate_limiter_service.cache.RateLimitCacheProvider;
import com.lasha.personal_api_rate_limiter_service.dto.RateLimitInfoResponse;
import com.lasha.personal_api_rate_limiter_service.encoding.Sha256Hasher;
import com.lasha.personal_api_rate_limiter_service.exceptions.ClientDisabledException;
import com.lasha.personal_api_rate_limiter_service.exceptions.InvalidApiKeyException;
import com.lasha.personal_api_rate_limiter_service.exceptions.InvalidUserIdException;
import com.lasha.personal_api_rate_limiter_service.exceptions.RateLimitExceededException;
import com.lasha.personal_api_rate_limiter_service.model.ClientEntity;
import com.lasha.personal_api_rate_limiter_service.pojo.RateLimitInfo;
import com.lasha.personal_api_rate_limiter_service.repository.ClientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final ClientRepository clientRepository;
    private final Sha256Hasher sha256Hasher;
    private final RateLimitCacheProvider rateLimitCacheProvider;

    @Value("${app.api-key-header}")
    private String apiKeyHeader;
    @Value("${app.user-id-header}")
    private String userIdHeader;

    public RateLimitService(ClientRepository clientRepository, Sha256Hasher sha256Hasher, RateLimitCacheProvider rateLimitCacheProvider) {
        this.clientRepository = clientRepository;
        this.sha256Hasher = sha256Hasher;
        this.rateLimitCacheProvider = rateLimitCacheProvider;
    }

    public RateLimitInfoResponse checkUserLimit(HttpServletRequest request) {
        String apiKey = request.getHeader(apiKeyHeader);
        String userId = request.getHeader(userIdHeader);
        System.out.println("Checking rate limit for API Key: " + apiKey + " and User ID: " + userId);
        if (apiKey == null || apiKey.isEmpty()) {
            throw new InvalidApiKeyException("API Key header is missing or empty");
        }

        if (userId == null || userId.isEmpty()) {
            throw new InvalidUserIdException("User ID header is missing or empty");
        }

        String hashedApiKey = sha256Hasher.hashToHex(apiKey);
        ClientEntity client = clientRepository.findClientByApiKeyHash(hashedApiKey);

        if (client == null) {
            throw new InvalidApiKeyException("Invalid API Key");
        }

        if (!client.isEnabled()) {
            throw new ClientDisabledException("Client is disabled");
        }

        int rateLimit = client.getRateLimit();
        long rateLimitWindowMillis = client.getRateWindowSeconds() * 1000;
        long now = System.currentTimeMillis();

        RateLimitInfo rateLimitInfo = rateLimitCacheProvider.get(hashedApiKey, userId);

        // If no rate limit info exists or the window has expired, create a new one
        if(rateLimitInfo == null || now > rateLimitInfo.getWindowEndMillis()) {
            rateLimitInfo = new RateLimitInfo(1, now + rateLimitWindowMillis);
            rateLimitCacheProvider.save(hashedApiKey, userId, rateLimitInfo);

            return new RateLimitInfoResponse(userId, rateLimit - 1);
        }

        // If the request count is within the limit, increment the count
        if(rateLimitInfo.getRequestCount() < rateLimit) {
            rateLimitInfo.setRequestCount(rateLimitInfo.getRequestCount() + 1);
            rateLimitCacheProvider.save(hashedApiKey, userId, rateLimitInfo);

            return new RateLimitInfoResponse(userId, rateLimit - rateLimitInfo.getRequestCount());
        }

        // If the request count exceeds the limit, return the remaining requests as 0
        throw new RateLimitExceededException("Rate limit exceeded for user: " + userId);
    }
}
