package com.lasha.personal_api_rate_limiter_service.interceptors;

import com.lasha.personal_api_rate_limiter_service.encoding.Sha256Hasher;
import com.lasha.personal_api_rate_limiter_service.exceptions.InvalidApiKeyException;
import com.lasha.personal_api_rate_limiter_service.model.ClientEntity;
import com.lasha.personal_api_rate_limiter_service.repository.ClientRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private final ClientRepository clientRepository;
    private final Sha256Hasher sha256Hasher;

    @Value("${app.api-key-header}")
    private String apiKeyHeader;

    public ApiKeyInterceptor(ClientRepository clientRepository, Sha256Hasher sha256Hasher) {
        this.clientRepository = clientRepository;
        this.sha256Hasher = sha256Hasher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String apiKey = request.getHeader(apiKeyHeader);
        System.out.println("Received API key: " + apiKey + " header: " + apiKeyHeader);
        if(apiKey == null || apiKey.isEmpty() == true) {
            throw new InvalidApiKeyException("API key is missing or empty");
        }

        String hashedApiKey = sha256Hasher.hashToHex(apiKey);

        ClientEntity clientEntity = clientRepository.findClientByApiKeyHash(hashedApiKey);
        if(clientEntity == null) {
            throw new InvalidApiKeyException("Invalid API key");
        }

        System.out.println("API key is valid for client: " + clientEntity.getEmail());
        return true;
    }
}
