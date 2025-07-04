package com.lasha.personal_api_rate_limiter_service.exceptions;

public class RateLimitExceededException extends RuntimeException{

    public RateLimitExceededException(String message) {
        super(message);
    }
}
