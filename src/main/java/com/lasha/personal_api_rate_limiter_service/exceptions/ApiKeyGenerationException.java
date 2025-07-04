package com.lasha.personal_api_rate_limiter_service.exceptions;

public class ApiKeyGenerationException extends RuntimeException {

    public ApiKeyGenerationException(String message) {
        super(message);
    }
}
