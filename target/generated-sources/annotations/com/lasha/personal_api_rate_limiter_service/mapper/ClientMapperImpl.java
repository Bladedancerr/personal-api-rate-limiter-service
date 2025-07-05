package com.lasha.personal_api_rate_limiter_service.mapper;

import com.lasha.personal_api_rate_limiter_service.dto.ClientLoginRequest;
import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterRequest;
import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterResponse;
import com.lasha.personal_api_rate_limiter_service.dto.ClientResponseDTO;
import com.lasha.personal_api_rate_limiter_service.model.ClientEntity;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-05T21:11:37+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientEntity fromRegisterRequest(ClientRegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setEmail( request.getEmail() );

        clientEntity.setEnabled( true );

        return clientEntity;
    }

    @Override
    public ClientEntity fromLoginRequest(ClientLoginRequest request) {
        if ( request == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setEmail( request.getEmail() );
        clientEntity.setPassword( request.getPassword() );

        return clientEntity;
    }

    @Override
    public ClientResponseDTO toResponseDto(ClientEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();

        clientResponseDTO.setId( entity.getId() );
        clientResponseDTO.setEmail( entity.getEmail() );
        clientResponseDTO.setApiKey( entity.getApiKey() );
        clientResponseDTO.setRateLimit( entity.getRateLimit() );
        clientResponseDTO.setRateWindowSeconds( (int) entity.getRateWindowSeconds() );
        clientResponseDTO.setEnabled( entity.isEnabled() );

        return clientResponseDTO;
    }

    @Override
    public ClientRegisterResponse toRegisterResponse(ClientEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String email = null;
        String apiKey = null;

        id = entity.getId();
        email = entity.getEmail();
        apiKey = entity.getApiKey();

        ClientRegisterResponse clientRegisterResponse = new ClientRegisterResponse( id, email, apiKey );

        return clientRegisterResponse;
    }
}
