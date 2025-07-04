package com.lasha.personal_api_rate_limiter_service.exceptions;

public class UserDisabledException extends RuntimeException {

    public UserDisabledException(String message) {
        super(message);
    }
}
