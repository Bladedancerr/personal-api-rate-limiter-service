package com.lasha.personal_api_rate_limiter_service.dto;

public class RateLimitInfoResponse {
    private String userId;
    private int remainingRequests;
    private long windowEndMillis;

    public RateLimitInfoResponse(String userId, int remainingRequests, long windowEndMillis) {
        this.userId = userId;
        this.remainingRequests = remainingRequests;
        this.windowEndMillis = windowEndMillis;
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

    public long getWindowEndMillis() {
        return windowEndMillis;
    }

    public void setWindowEndMillis(long windowEndMillis) {
        this.windowEndMillis = windowEndMillis;
    }
}
