package com.lasha.personal_api_rate_limiter_service.service;

import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterResponse;
import com.lasha.personal_api_rate_limiter_service.encoding.ApiKeyGenerator;
import com.lasha.personal_api_rate_limiter_service.encoding.Sha256Hasher;
import com.lasha.personal_api_rate_limiter_service.exceptions.EmailAlreadyExistsException;
import com.lasha.personal_api_rate_limiter_service.mapper.ClientMapper;
import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterRequest;
import com.lasha.personal_api_rate_limiter_service.exceptions.InvalidCredentialsException;
import com.lasha.personal_api_rate_limiter_service.model.ClientEntity;
import com.lasha.personal_api_rate_limiter_service.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ApiKeyGenerator apiKeyGenerator;
    private final Sha256Hasher sha256Hasher;
    private final ClientMapper clientMapper;

    @Value("${app.client.default-rate-limit}")
    private int defaultRateLimit;
    @Value("${app.client.default-rate-window-sec}")
    private int defaultRateWindowSeconds;

    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ClientMapper clientMapper, ApiKeyGenerator apiKeyGenerator, Sha256Hasher sha256Hasher) {
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.clientMapper = clientMapper;
        this.apiKeyGenerator = apiKeyGenerator;
        this.sha256Hasher = sha256Hasher;
    }


    public ClientRegisterResponse registerClient(ClientRegisterRequest clientRegisterRequest) {

        if (clientRegisterRequest.getEmail() == null || clientRegisterRequest.getEmail().trim().isEmpty() ||
                clientRegisterRequest.getPassword() == null || clientRegisterRequest.getPassword().trim().isEmpty()) {
            throw new InvalidCredentialsException("Email or password is missing");
        }

        // Check for existing email
        if (clientRepository.countByEmail(clientRegisterRequest.getEmail().trim()) > 0) {
            throw new EmailAlreadyExistsException("Client with this email already exists");
        }

        ClientEntity entity = clientMapper.fromRegisterRequest(clientRegisterRequest);

        // Encode password and generate API key
        entity.setPassword(bCryptPasswordEncoder.encode(clientRegisterRequest.getPassword().trim()));

        String generatedApiKey = apiKeyGenerator.generateApiKey();
        entity.setApiKey(sha256Hasher.hashToHex(generatedApiKey));

        // Set default values from properties
        entity.setRateLimit(defaultRateLimit);
        entity.setRateWindowSeconds(defaultRateWindowSeconds);

        entity.setEnabled(true);

        entity.setCreatedAt(Instant.now());

        // Save entity
        ClientEntity savedEntity = clientRepository.save(entity);

        // Prepare response with raw API key
        ClientRegisterResponse response = clientMapper.toRegisterResponse(savedEntity);
        response.setApiKey(generatedApiKey);

        return response;
    }
}
