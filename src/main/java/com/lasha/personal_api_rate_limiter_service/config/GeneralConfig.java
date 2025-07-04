package com.lasha.personal_api_rate_limiter_service.config;

import com.lasha.personal_api_rate_limiter_service.encoding.ApiKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class GeneralConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public ApiKeyGenerator apiKeyGenerator() { return new ApiKeyGenerator(); }
}
