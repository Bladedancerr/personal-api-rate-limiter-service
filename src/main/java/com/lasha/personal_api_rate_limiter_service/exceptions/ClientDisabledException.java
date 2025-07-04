package com.lasha.personal_api_rate_limiter_service.exceptions;

public class ClientDisabledException extends RuntimeException {

    public ClientDisabledException(String message) {
        super(message);
    }
}
