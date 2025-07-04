package com.lasha.personal_api_rate_limiter_service.exceptions;

public class InvalidApiKeyException extends  RuntimeException {

    public InvalidApiKeyException(String message) {
        super(message);
    }
}
