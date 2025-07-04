package com.lasha.personal_api_rate_limiter_service.mapper;

import com.lasha.personal_api_rate_limiter_service.dto.ClientLoginRequest;
import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterRequest;
import com.lasha.personal_api_rate_limiter_service.dto.ClientRegisterResponse;
import com.lasha.personal_api_rate_limiter_service.dto.ClientResponseDTO;
import com.lasha.personal_api_rate_limiter_service.model.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    // Mapper interface for converting between ClientRegisterRequest to ClientEntity
    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "apiKey", ignore = true),
            @Mapping(target = "rateLimit", ignore = true),
            @Mapping(target = "rateWindowSeconds", ignore = true),
            @Mapping(target = "enabled", constant = "true"),
            @Mapping(target = "createdAt", ignore = true)
    })
    ClientEntity fromRegisterRequest(ClientRegisterRequest request);

    // Mapper interface for converting between ClientLoginRequest to ClientEntity
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "apiKey", ignore = true),
            @Mapping(target = "rateLimit", ignore = true),
            @Mapping(target = "rateWindowSeconds", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "createdAt", ignore = true)
    })
    ClientEntity fromLoginRequest(ClientLoginRequest request);

    // Mapper interface for converting between ClientEntity to ClientResponseDTO
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "apiKey", target = "apiKey"),
            @Mapping(source = "rateLimit", target = "rateLimit"),
            @Mapping(source = "rateWindowSeconds", target = "rateWindowSeconds"),
            @Mapping(source = "enabled", target = "enabled")
    })
    ClientResponseDTO toResponseDto(ClientEntity entity);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "email", target = "email"),
    })
    ClientRegisterResponse toRegisterResponse(ClientEntity entity);
}
