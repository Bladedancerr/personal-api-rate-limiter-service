package com.lasha.personal_api_rate_limiter_service.dto;

import java.util.UUID;

public class ClientRegisterResponse {
    private UUID id;
    private String email;
    private String apiKey;

    public ClientRegisterResponse(UUID id, String email, String apiKey) {
        this.id = id;
        this.email = email;
        this.apiKey = apiKey;
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
}

