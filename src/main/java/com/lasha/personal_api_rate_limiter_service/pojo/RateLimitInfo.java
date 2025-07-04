package com.lasha.personal_api_rate_limiter_service.pojo;

public class RateLimitInfo {
    private int requestCount;
    private long windowEndMillis;

    public RateLimitInfo(int requestCount, long windowEndMillis) {
        this.requestCount = requestCount;//number of requests made in the current window
        this.windowEndMillis = windowEndMillis;//end of the current window in milliseconds
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public long getWindowEndMillis() {
        return windowEndMillis;
    }

    public void setWindowEndMillis(long windowEndMillis) {
        this.windowEndMillis = windowEndMillis;
    }
}
