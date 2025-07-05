package com.lasha.personal_api_rate_limiter_service.exceptions;

public class UserLimitExceededException extends RuntimeException {

    public UserLimitExceededException(String message) {
        super(message);
    }
}
