package com.lasha.personal_api_rate_limiter_service.dto;

public class RateLimitInfoResponse {
    String userId;
    int remainingRequests;

    public RateLimitInfoResponse(String userId, int remainingRequests) {
        this.userId = userId;
        this.remainingRequests = remainingRequests;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRemainingRequests() {
        return remainingRequests;
    }

    public void setRemainingRequests(int remainingRequests) {
        this.remainingRequests = remainingRequests;
    }
}
