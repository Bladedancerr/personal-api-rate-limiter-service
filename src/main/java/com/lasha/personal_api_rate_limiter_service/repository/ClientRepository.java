package com.lasha.personal_api_rate_limiter_service.repository;

import com.lasha.personal_api_rate_limiter_service.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM client WHERE email = :email)", nativeQuery = true)
    long countByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM client WHERE api_key = :keyHash", nativeQuery = true)
    ClientEntity findClientByApiKeyHash(@Param("keyHash") String keyHash);
}
