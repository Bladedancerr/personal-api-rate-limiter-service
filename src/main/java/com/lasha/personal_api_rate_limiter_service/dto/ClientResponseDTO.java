package com.lasha.personal_api_rate_limiter_service.dto;

import java.util.UUID;

public class ClientResponseDTO {
    private UUID id;
    private String email;
    private String apiKey;
    private int rateLimit;
    private int rateWindowSeconds;
    private boolean enabled;

    public ClientResponseDTO() {
    }

    public ClientResponseDTO(
            UUID id,
            String email,
            String apiKey,
            int rateLimit,
            int rateWindowSeconds,
            boolean enabled) {

        this.id = id;
        this.email = email;
        this.apiKey = apiKey;
        this.rateLimit = rateLimit;
        this.rateWindowSeconds = rateWindowSeconds;
        this.enabled = enabled;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(int rateLimit) {
        this.rateLimit = rateLimit;
    }

    public int getRateWindowSeconds() {
        return rateWindowSeconds;
    }

    public void setRateWindowSeconds(int rateWindowSeconds) {
        this.rateWindowSeconds = rateWindowSeconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
