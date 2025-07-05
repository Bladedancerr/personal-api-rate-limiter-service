package com.lasha.personal_api_rate_limiter_service.service;

import com.lasha.personal_api_rate_limiter_service.cache.RateLimitStore;
import com.lasha.personal_api_rate_limiter_service.dto.RateLimitInfoResponse;
import com.lasha.personal_api_rate_limiter_service.encoding.Sha256Hasher;
import com.lasha.personal_api_rate_limiter_service.exceptions.*;
import com.lasha.personal_api_rate_limiter_service.model.ClientEntity;
import com.lasha.personal_api_rate_limiter_service.repository.ClientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final ClientRepository clientRepository;
    private final Sha256Hasher sha256Hasher;
    private final RateLimitStore rateLimitStore;
    // Use intern pool for lock keys to ensure proper synchronization
    private final ConcurrentHashMap<String, String> lockKeyPool = new ConcurrentHashMap<>();

    @Value("${app.api-key-header}")
    private String apiKeyHeader;
    @Value("${app.user-id-header}")
    private String userIdHeader;

    @Autowired
    public RateLimitService(ClientRepository clientRepository, Sha256Hasher sha256Hasher, RateLimitStore rateLimitStore) {
        this.clientRepository = clientRepository;
        this.sha256Hasher = sha256Hasher;
        this.rateLimitStore = rateLimitStore;
    }

    public RateLimitInfoResponse checkUserLimit(HttpServletRequest request) {
        String apiKey = request.getHeader(apiKeyHeader);
        String userId = request.getHeader(userIdHeader);

        if (apiKey == null || apiKey.isEmpty()) {
            throw new InvalidApiKeyException("API key is missing or empty");
        }

        if (userId == null || userId.isEmpty()) {
            throw new InvalidUserIdException("User ID is missing or empty");
        }

        ClientEntity clientEntity = getValidClientEntity(apiKey);

        int rateLimit = clientEntity.getRateLimit();
        int userLimit = clientEntity.getUserLimit();
        long windowSeconds = clientEntity.getRateWindowSeconds();
        String clientId = clientEntity.getId().toString();

        // Get interned lock key for proper synchronization
        String lockKey = lockKeyPool.computeIfAbsent(clientId, k -> k); // Lock on clientId for user limit check

        synchronized (lockKey) {
            // Check if this is a new user (no existing rate limit key)
            long ttl = rateLimitStore.getTtl(clientId, userId);
            boolean isNewUser = (ttl == 0);

            // For new users, check if adding them would exceed the limit
            if (isNewUser) {
                // Check current count BEFORE adding this user
                int currentUserCount = rateLimitStore.getUniqueUserCount(clientId);

                // If adding this user would exceed the limit, reject
                if (currentUserCount >= userLimit) {
                    throw new UserLimitExceededException("User limit exceeded for this client. Maximum " + userLimit + " users allowed.");
                }

                // Start new window for this user
                int count = resetAndStartWindow(clientId, userId, windowSeconds);
                ttl = windowSeconds * 1000L;

                int remainingRequests = rateLimit - count;
                long remainingTimeSeconds = ttl / 1000;
                return new RateLimitInfoResponse(userId, remainingRequests, remainingTimeSeconds);
            } else {
                // Existing user - just increment their counter
                rateLimitStore.addOrUpdateUserActivity(clientId, userId);
                int count = rateLimitStore.incrementRequestRate(clientId, userId);

                if (count > rateLimit) {
                    throw new RateLimitExceededException("Rate limit exceeded");
                }

                int remainingRequests = rateLimit - count;
                long remainingTimeSeconds = ttl / 1000;
                return new RateLimitInfoResponse(userId, remainingRequests, remainingTimeSeconds);
            }
        }
    }

    private ClientEntity getValidClientEntity(String apiKey) {
        String hashedApiKey = sha256Hasher.hashToHex(apiKey);
        ClientEntity clientEntity = clientRepository.findClientByApiKeyHash(hashedApiKey);

        if (clientEntity == null) {
            throw new UserNotFoundException("Client not found for the provided API key");
        }

        if (!clientEntity.isEnabled()) {
            throw new UserDisabledException("Client is disabled");
        }

        return clientEntity;
    }

    private int resetAndStartWindow(String clientId, String userId, long windowSeconds) {
        // Don't call resetRequestRate for new users as it removes them from active set
        // Just start fresh with increment and TTL
        int count = rateLimitStore.incrementRequestRate(clientId, userId);
        rateLimitStore.setTtl(clientId, userId, windowSeconds * 1000L);
        rateLimitStore.addOrUpdateUserActivity(clientId, userId);
        return count;
    }
}