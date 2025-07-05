package com.lasha.personal_api_rate_limiter_service.schedulers;

import com.lasha.personal_api_rate_limiter_service.cache.RateLimitStore;
import com.lasha.personal_api_rate_limiter_service.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Scheduled task to clean up expired users from rate limit store.
 * Runs periodically to remove users whose rate windows have expired.
 */
@Component
public class RateLimitCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(RateLimitCleanupScheduler.class);

    private final RateLimitStore rateLimitStore;
    private final ClientRepository clientRepository;

    @Autowired
    public RateLimitCleanupScheduler(RateLimitStore rateLimitStore, ClientRepository clientRepository) {
        this.rateLimitStore = rateLimitStore;
        this.clientRepository = clientRepository;
    }

    /**
     * Runs every 30 seconds to clean up expired users.
     * You can adjust the schedule based on your needs.
     */
    @Scheduled(fixedDelay = 15000) // 30 seconds
    public void cleanupExpiredUsers() {
        log.debug("Starting scheduled cleanup of expired users");

        try {
            // Get all active client IDs
            List<String> clientIds = clientRepository.findAll()
                    .stream()
                    .map(client -> client.getId().toString())
                    .collect(Collectors.toList());

            int totalCleaned = 0;

            // Clean up expired users for each client
            for (String clientId : clientIds) {
                try {
                    int beforeCount = rateLimitStore.getUniqueUserCount(clientId);
                    rateLimitStore.cleanupExpiredUsers(clientId);
                    int afterCount = rateLimitStore.getUniqueUserCount(clientId);

                    int cleaned = beforeCount - afterCount;
                    if (cleaned > 0) {
                        totalCleaned += cleaned;
                        log.info("Cleaned {} expired users for client {}", cleaned, clientId);
                    }
                } catch (Exception e) {
                    log.error("Error cleaning up users for client {}: {}", clientId, e.getMessage());
                }
            }

            if (totalCleaned > 0) {
                log.info("Scheduled cleanup completed. Total expired users removed: {}", totalCleaned);
            } else {
                log.debug("Scheduled cleanup completed. No expired users found.");
            }

        } catch (Exception e) {
            log.error("Error during scheduled cleanup: {}", e.getMessage(), e);
        }
    }
}