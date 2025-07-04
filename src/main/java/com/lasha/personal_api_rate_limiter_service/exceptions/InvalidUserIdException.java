package com.lasha.personal_api_rate_limiter_service.exceptions;

public class InvalidUserIdException extends RuntimeException {

    public InvalidUserIdException(String message) {
        super(message);
    }
}
